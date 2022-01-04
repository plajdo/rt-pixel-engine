package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.util.Config;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Camera {

    private Map map;

    private final Vector2 position;
    private final Vector2 direction;
    private final Vector2 plane;

    private final Vector2 viewportSize;
    private Rectangle screenSize;

    public Vector2 movementVector = Vector2.Zero;
    public float speed = 1.5f;

    // plane center = position + direction
    // plane right end = pos + dir + plane
    // plane left end = pos + dir - plane

    // fov = length(dir) : length(plane)

    public Camera(Map map, Vector2 position, Vector2 direction) {
        this.position = position;
        this.direction = direction;
        this.plane = direction.cpy().rotate90(-1).scl(2f / 3f);

        this.map = map;

        this.viewportSize = new Vector2(160, 90);
        this.screenSize = new Rectangle(160, 90);
    }

    public void rotate(float angleDeg) {
        direction.rotateDeg(angleDeg);
        plane.rotateDeg(angleDeg);
    }

    public void tick(float dt) {
        position.add(movementVector.cpy().scl(dt).scl(speed).rotateRad(direction.angleRad()));
    }


    public void draw(Graphics graphics) {
        synchronized(this) {
            drawWalls(graphics);
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private void drawWalls(Graphics graphics) {
        BufferedImage bufferedImage = new BufferedImage(
                (int) viewportSize.x,
                (int) viewportSize.y,
                BufferedImage.TYPE_INT_ARGB
        );
        final int[] screenBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        for(int i = 0; i < (int) viewportSize.x; i++) {
            float screenX = 2f * i / (int) viewportSize.x - 1f;
            Vector2 rayDirection = new Vector2(
                    this.direction.x + plane.x * screenX,
                    this.direction.y + plane.y * screenX
            ).nor();

            var ray = new BlockRay(this.position, rayDirection, map);
            var zBuffer = ray.cast(Config.Display.RENDER_DISTANCE);
            //System.out.println(zBuffer.size());

            for(int j = zBuffer.size() - 1; j >= 0; j--) {
                Ray.Hit<Block> hit = zBuffer.get(j);

                // Fish eye fix
                float distance = (float) (hit.distance() * Math.abs(Math.sin(plane.angleRad() - rayDirection.angleRad())));
                float hitSide = hit.position().x % 1 == 0 ? 0 : 1;

                // Texture coordinates
                Vector2 hitPosition = hit.position();
                Vector2 wallHitCoordinates = hitPosition.cpy()
                        .sub((float) Math.floor(hitPosition.x), (float) Math.floor(hitPosition.y));

                int texelX_X = Texture.SIZE - (int)(wallHitCoordinates.x * Texture.SIZE) - 1;
                int texelX_Y = Texture.SIZE - (int)(wallHitCoordinates.y * Texture.SIZE) - 1;
                int texelX = Math.min(texelX_X, texelX_Y);

                int screenHeight = (int) viewportSize.y;
                int marginalObjectHeight = (int) (screenHeight / distance);

                int horizon = screenHeight / 2;
                int eyeLevel = marginalObjectHeight / 2;
                int floorLevel = eyeLevel + horizon;

                float objectHeight = hit.result().getHeight() * marginalObjectHeight;
                int objectTop = (int) (floorLevel - objectHeight / 2);
                int objectBottom = floorLevel;

                float texelStep = 1f * Texture.SIZE / marginalObjectHeight;
                float texelPosition = texelStep;

                float colorScale = 1 - (hitSide * 0.33f);

                for(int k = objectTop; k < objectBottom; k++) {
                    int texelY = (int)texelPosition & (Texture.SIZE - 1);
                    texelPosition += texelStep;

                    Texture texture = hit.result().getTexture(hitSide);
                    Color color = texture.getRGB(texelX, texelY);

                    if(k >= 0 && k < (int) viewportSize.y) {
                        int currentColor = screenBuffer[i + k * (int) viewportSize.x];
                        screenBuffer[i + k * (int) viewportSize.x] = color.fade(currentColor).scaled(colorScale).argb();
                    }
                }
            }
        }

        graphics.setColor(java.awt.Color.green);
        graphics.drawImage(bufferedImage, 0, 0, screenSize.width, screenSize.height, null);
        graphics.drawString(this.position.toString(), 0, 50);
    }

    public void setScreenSize(Rectangle size) {
        synchronized(this) {
            this.screenSize = size;
            this.viewportSize.set(
                    size.width * Config.Display.DRAWING_QUALITY,
                    size.height * Config.Display.DRAWING_QUALITY
            );
        }
    }

}
