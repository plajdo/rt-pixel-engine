package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.blocks.BlockManager;
import sk.bytecode.bludisko.rt.game.entities.Entity;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
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

    private float positionZ;
    private float pitch;

    private final Vector2 viewportSize;
    private Rectangle screenSize;

    public Vector2 movementVector = Vector2.Zero;
    public float speed = 1.5f;

    public Camera(Map map, Vector2 position, Vector2 direction) {
        this.position = position;
        this.direction = direction;
        this.plane = direction.cpy().rotate90(-1).scl(2f / 3f);
        this.pitch = 0f;
        this.positionZ = 0f;

        this.map = map;

        this.viewportSize = new Vector2(160, 90);
        this.screenSize = new Rectangle(160, 90);
    }

    public void rotate(float angleDeg) {
        direction.rotateDeg(angleDeg);
        plane.rotateDeg(angleDeg);
    }

    public void setPitch(float pitch) {
        this.pitch += pitch;
        if(this.pitch < -200) {
            this.pitch = -200;
        }
        if(this.pitch > 200) {
            this.pitch = 200;
        }
    }

    public void tick(float dt) {
        position.add(movementVector.cpy().scl(dt).scl(speed).rotateRad(direction.angleRad()));
    }

    public void draw(Graphics graphics) {
        BufferedImage bufferedImage = new BufferedImage(
                (int) viewportSize.x,
                (int) viewportSize.y,
                BufferedImage.TYPE_INT_ARGB
        );
        final int[] screenBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        synchronized(this) {
            drawFloor(screenBuffer);
            drawWalls(screenBuffer);
        }

        int width = (int) (viewportSize.x / Config.Display.DRAWING_QUALITY);
        int height = (int) (viewportSize.y / Config.Display.DRAWING_QUALITY);
        int imgTopLeftX = (screenSize.width / 2) - (width / 2);
        int imgTopLeftY = (screenSize.height / 2) - (height / 2);

        graphics.setColor(java.awt.Color.green);
        graphics.drawImage(bufferedImage, imgTopLeftX, imgTopLeftY, width, height, null);
        graphics.drawString(this.position.toString(), 0, 50);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private void drawWalls(final int[] screenBuffer) {
        for(int i = 0; i < (int) viewportSize.x; i++) {
            float screenX = 2f * i / (int) viewportSize.x - 1f;
            Vector2 rayDirection = new Vector2(
                    this.direction.x + plane.x * screenX,
                    this.direction.y + plane.y * screenX
            ).nor();

            var ray = new BlockRay(this.position, rayDirection, map);
            var zBuffer = ray.cast(Config.Display.RENDER_DISTANCE);

            for(int j = zBuffer.size() - 1; j >= 0; j--) {
                Ray.Hit<Block> hit = zBuffer.get(j);

                // Fish eye fix
                float distance = (float) (hit.distance() * Math.abs(Math.sin(plane.angleRad() - rayDirection.angleRad())));
                float hitSide = hit.position().x % 1 == 0 ? 0 : 1;

                Vector2 hitPosition = hit.position();
                Vector2 wallHitCoordinates = hitPosition.cpy()
                        .sub((float) Math.floor(hitPosition.x), (float) Math.floor(hitPosition.y));

                int texelX_X = Texture.WIDTH - (int)(wallHitCoordinates.x * Texture.WIDTH) - 1;
                int texelX_Y = Texture.WIDTH - (int)(wallHitCoordinates.y * Texture.WIDTH) - 1;
                int texelX = Math.min(texelX_X, texelX_Y);

                int screenWidth = (int) viewportSize.x;
                int screenHeight = (int) viewportSize.y;
                int marginalObjectHeight = (int) (screenHeight / distance);

                int horizon = screenHeight / 2;
                int eyeLevel = marginalObjectHeight / 2;
                int floorLevel = eyeLevel + horizon;

                float objectHeight = hit.result().getHeight() * marginalObjectHeight;
                int objectTop = (int) ((floorLevel - objectHeight / 2) + pitch);
                int objectBottom = (int) (floorLevel + pitch);

                float texelStep = 1f * Texture.HEIGHT / marginalObjectHeight;
                float texelPosition = texelStep;

                float colorScale = 1 - (hitSide * 0.33f);

                for(int k = objectTop; k < objectBottom; k++) {
                    int texelY = (int)texelPosition & (Texture.WIDTH - 1);
                    texelPosition += texelStep;

                    Texture texture = hit.result().getTexture(hitSide);
                    Color color = texture.getColor(texelX, texelY);

                    if(k >= 0 && k < screenHeight) {
                        int currentColor = screenBuffer[i + k * screenWidth];
                        screenBuffer[i + k * screenWidth] = color.fade(currentColor).scaled(colorScale).argb();
                    }
                }
            }
        }
    }

    private void drawFloor(final int[] screenBuffer) {
        Vector2 rayDirectionLeft = new Vector2(direction).sub(plane);
        Vector2 rayDirectionRight = new Vector2(direction).add(plane);

        int screenWidth = (int) viewportSize.x;
        int screenHeight = (int) viewportSize.y;

        int horizon = screenHeight / 2;

        for(int y = horizon + (int) pitch; y < (int) viewportSize.y; y++) {
            if(y < 0) continue;

            float cameraY = y - screenHeight / 2f - pitch;
            float cameraZ = 0.5f * screenHeight + positionZ;

            float floorDistance = cameraZ / cameraY;

            Vector2 marginalFloorDistance = new Vector2(floorDistance, floorDistance)
                    .scl(rayDirectionRight.cpy().sub(rayDirectionLeft))
                    .scl(1f / screenWidth);

            Vector2 floorCoordinates = new Vector2(position)
                    .add(rayDirectionLeft.cpy().scl(floorDistance));

            for(int x = 0; x < screenWidth; x++) {
                int tileX = (int)floorCoordinates.x;
                int tileY = (int)floorCoordinates.y;

                Vector2 floorTilePosition = MathUtils.decimalPart(floorCoordinates);
                int textureX = (int) Math.min(Texture.WIDTH * floorTilePosition.x, Texture.WIDTH - 1);
                int textureY = (int) Math.min(Texture.HEIGHT * floorTilePosition.y, Texture.HEIGHT - 1);

                floorCoordinates.add(marginalFloorDistance);

                int color = BlockManager.getBlock(1).getTexture(0).getColor(textureX, textureY).argb();

                screenBuffer[x + y * screenWidth] = color;
            }
        }
    }

    public void setScreenSize(Rectangle size) {
        synchronized(this) {
            this.screenSize = size;

            var width = Math.max(size.width, size.height);
            var height = width / 1.33333f;

            this.viewportSize.set(
                    width * Config.Display.DRAWING_QUALITY,
                    height * Config.Display.DRAWING_QUALITY
            );
        }
    }

}
