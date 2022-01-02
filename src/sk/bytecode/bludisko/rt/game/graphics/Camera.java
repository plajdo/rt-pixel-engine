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

            int height = screenSize.height;
            int objectHeight = (int) (height / distance);

            // Textures here:
            float wallX; //where the wall was hit //TODO: remove comments
            float wallY;
            if(hitSide == 0) {
                wallX = position.y + distance * (direction.y + plane.y * (2f * i / rayCount - 1f)); //rayDirY
            } else {
                wallX = position.x + distance * (direction.x + plane.x * (2f * i / rayCount - 1f)); //rayDirX
            }
            wallX = ray.getPosition().x;
            wallX -= Math.floor(wallX);

            wallY = ray.getPosition().y;
            wallY -= Math.floor(wallY);

            int texelX = Texture.SIZE - (int)(wallX * Texture.SIZE) - 1;
            int texelX2 = Texture.SIZE - (int)(wallY * Texture.SIZE) - 1;

            // TODO: why this? how this? understand plz
            //x coordinate on the texture
            //int texelX;// = Texture.SIZE - (int)(wallX * Texture.SIZE) - 1;
            /*if(hitSide == 0 && (direction.x + plane.x * (2f * i / rayCount - 1f)) > 0) {
                texelX = Texture.SIZE - texelX - 1;
            }
            if(hitSide == 1 && (direction.y + plane.y * (2f * i / rayCount - 1f)) < 0) {
                texelX = Texture.SIZE - texelX - 1;
            }*/

            //how much to increase x coordinate per screen pixel
            float step = 1f * 64 / objectHeight;
            float texPos = ((-objectHeight / 2f + height / 2f) - height / 2f + objectHeight / 2f) * step;

            float colorScale = 1 - (hitSide * 0.33f);

            for(int y = (-objectHeight / 2 + height / 2); y < (height / 2 + objectHeight / 2); y++) {
                int texelY = (int)texPos & (64 - 1);
                texPos += step;

                Texture texture = hit.block().getTexture(hitSide);
                Color color = texture.getRGB(Math.min(texelX, texelX2), texelY);

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
            graphics.fillRect(i, height / 2 - objectHeight * blockHeightMultiplier / 2, 1, (int) (objectHeight * ((blockHeightMultiplier != 1) ? 3f/2f : 1f)));
        }
    }
     */

    public void setScreenSize(Rectangle newScreenSize) {
        this.screenSize = newScreenSize;
    }

}
