package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

public class Camera {

    private Map map;

    private final Vector2 position; //TODO: better access control
    private final Vector2 direction;
    private final Vector2 plane;

    private Rectangle screenSize;

    public Vector2 movementVector = Vector2.Zero;
    public float speed = 1.5f;

    private final int rayCount;

    // plane center = position + direction
    // plane right end = pos + dir + plane
    // plane left end = pos + dir - plane

    // fov = length(dir) : length(plane)

    public Camera(Map map, Vector2 position, Vector2 direction, Rectangle screenSize) {
        this.position = position;
        this.direction = direction;
        this.plane = direction.cpy().rotate90(-1).scl(2f / 3f);

        this.map = map;

        this.screenSize = screenSize;
        this.rayCount = 640;
    }

    public void rotate(float angleDeg) {
        direction.rotateDeg(angleDeg);
        plane.rotateDeg(angleDeg);
    }

    public void tick(float dt) {
        position.add(movementVector.cpy().scl(dt).scl(speed).rotateRad(direction.angleRad()));
    }

    public void draw(Graphics graphics) {
        BufferedImage bufferedImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        final int[] screenBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        for(int i = 0; i < rayCount; i++) {
            float screenX = 2f * i / rayCount - 1f;
            Vector2 rayDirection = new Vector2(
                    this.direction.x + plane.x * screenX,
                    this.direction.y + plane.y * screenX
            ).nor();

            var ray = new Ray(this.map, this.position, rayDirection);
            var hit = ray.cast2();

            // Fish eye fix
            float distance = (float) (hit.distance() * Math.abs(Math.sin(plane.angleRad() - rayDirection.angleRad())));
            float hitSide = hit.side();

            // Texture coordinates
            Vector2 hitPosition = ray.getPosition();
            Vector2 wallHitCoordinates = hitPosition.cpy()
                    .sub((float) Math.floor(hitPosition.x), (float) Math.floor(hitPosition.y));

            int texelX_X = Texture.SIZE - (int)(wallHitCoordinates.x * Texture.SIZE) - 1;
            int texelX_Y = Texture.SIZE - (int)(wallHitCoordinates.y * Texture.SIZE) - 1;
            int texelX = Math.min(texelX_X, texelX_Y);

            int screenHeight = screenSize.height;
            int objectHeight = (int) (screenHeight / distance);

            int horizon = screenHeight / 2;
            int objectCenter = objectHeight / 2;
            int objectTop = -objectCenter + horizon;
            int objectBottom = objectCenter + horizon;

            float texelStep = 1f * Texture.SIZE / objectHeight;
            //float texPos = (objectTop - horizon + objectCenter) * texelStep;
            float texPos = texelStep;

            float colorScale = 1 - (hitSide * 0.33f);

            for(int y = objectTop; y < objectBottom; y++) {
                int texelY = (int)texPos & (64 - 1);
                texPos += texelStep;

                Texture texture = hit.block().getTexture(hitSide);
                Color color = texture.getRGB(Math.min(texelX_X, texelX_Y), texelY);

                if(y >= 0 && y < 480) {
                    screenBuffer[i + y * 640] = color.scaled(colorScale);
                }
            }
        }

        graphics.setColor(java.awt.Color.green);
        graphics.drawImage(bufferedImage, 0, 0, null);
        graphics.drawString(this.position.toString(), 0, 50);
    }

    //TODO: kept because of block height multiplier
    /*
            if(side == 1) {
                c = c.darker();
            }
            graphics.setColor(c);
            graphics.fillRect(i,
            height / 2 - objectHeight * blockHeightMultiplier / 2,
            1,
            (int) (objectHeight * ((blockHeightMultiplier != 1) ? 3f/2f : 1f))
            );
        }
    }
     */

    public void setScreenSize(Rectangle newScreenSize) {
        this.screenSize = newScreenSize;
    }

}
