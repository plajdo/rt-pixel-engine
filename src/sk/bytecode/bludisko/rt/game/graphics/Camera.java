package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.graphics.texture.TextureManager;
import sk.bytecode.bludisko.rt.game.map.GameMap;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.map.world.GameWorld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

public class Camera {

    private Map map;

    private Vector2 position; //TODO: better access control
    private Vector2 direction;
    private Vector2 plane;

    public Vector2 movementVector = Vector2.Zero;
    public float speed = 1.5f;

    private final int rayCount;
    private final List<Ray> rays;

    // plane center = position + direction
    // plane right end = pos + dir + plane
    // plane left end = pos + dir - plane

    // fov = length(dir) : length(plane)

    public Camera(Map map, Vector2 position, Vector2 direction, float fov, int rayCount) {
        this.position = position;
        this.direction = direction;
        this.map = map;
        this.setFieldOfView(fov);

        this.rayCount = rayCount;
        this.rays = new ArrayList<>(rayCount);
    }

    public void setFieldOfView(float degrees) {
        float halfFOV = degrees * 0.5f * MathUtils.degreesToRadians;
        float halfPlaneLength = MathUtils.sin(halfFOV) / MathUtils.sin(MathUtils.PI - halfFOV);

        //this.plane = new Vector2(0f, halfPlaneLength)
        //        .rotateRad(direction.angleRad());
        // TODO: FIX FIX FIX

        this.plane = new Vector2(0.66f, 0f);

    }

    public void rotate(float angle) {
        float oldx = getDirection().x;
        getDirection().x = (float) (getDirection().x * Math.cos(MathUtils.degreesToRadians * angle) - getDirection().y * Math.sin(MathUtils.degreesToRadians * angle));
        getDirection().y = (float) (oldx * Math.sin(MathUtils.degreesToRadians * angle) + getDirection().y * Math.cos(MathUtils.degreesToRadians * angle));

        float oldplanex = getPlane().x;
        getPlane().x = (float) (getPlane().x * Math.cos(MathUtils.degreesToRadians * angle) - getPlane().y * Math.sin(MathUtils.degreesToRadians * angle));
        getPlane().y = (float) (oldplanex * Math.sin(MathUtils.degreesToRadians * angle) + getPlane().y * Math.cos(MathUtils.degreesToRadians * angle));
    }

    public void tick(float dt) {
        getPosition().add(movementVector.cpy().scl(dt).scl(speed).rotateRad(getDirection().angleRad()));
    }

    public void draw(Graphics graphics) {
        //int[] image = new int[640 * 480];

        for(int i = 0; i < rayCount; i++) {
            float screenX = 2f * i / rayCount - 1f;
            Vector2 rayDirection = new Vector2(
                    this.direction.x + plane.x * screenX,
                    this.direction.y + plane.y * screenX
            ).nor();

            var ray = new Ray(this.map, this.position, rayDirection);
            float rayDistance = ray.cast2();
            float distance = rayDistance;//(float) (rayDistance * Math.sin(Math.PI / 2 - rayDirection.angleRad()));

            int hitSide = ray.getSide();

            int hitX = (int) ray.getTile().x;
            int hitY = (int) ray.getTile().y;

            int height = 480; // TODO: FIX
            int objectHeight = (int) (height / distance);

            // Textures here:
            int textureNumber = map.getTile(hitX, hitY);

            float wallX; //where the wall was hit //TODO: remove comments
            if(hitSide == 0) {
                wallX = position.y + distance * (direction.y + plane.y * (2f * i / rayCount - 1f)); //rayDirY
            } else {
                wallX = position.x + distance * (direction.x + plane.x * (2f * i / rayCount - 1f)); //rayDirX
            }

            wallX -= Math.floor(wallX);


            // TODO: why this? how this? understand plz
            //x coordinate on the texture
            int texelX = (int)(wallX * 64);
            if(hitSide == 0 && (direction.x + plane.x * (2f * i / rayCount - 1f)) > 0) {
                texelX = 64 - texelX - 1;
            }
            if(hitSide == 1 && (direction.y + plane.y * (2f * i / rayCount - 1f)) < 0) {
                texelX = 64 - texelX - 1;
            }


            //how much to increase x coordinate per screen pixel
            float step = 1f * 64 / objectHeight;
            float texPos = ((-objectHeight / 2f + height / 2f) - height / 2f + objectHeight / 2f) * step;
/*
            for(int y = (-objectHeight / 2 + height / 2); y < (height / 2 + objectHeight / 2); y++) {
                int texelY = (int)texPos & (64 - 1);
                texPos += step;
                int color = TextureManager.getTexture(textureNumber).getRGB(texelX, texelY);

                if(hitSide == 1) {
                    color = (color >> 1) & 0b011111110111111101111111;
                }

                if(y >= 0 && y < 480) {
                    image[i + y * 640] = color;
                }
                //graphics.setColor(new Color(color)); //TODO: IMPORTANT: USE BUFFERED IMAGE TO DRAW INSTEAD - done????
                //graphics.fillRect(i, y, 1, 1);

            }
*/

            graphics.setColor(Color.green);
            if(hitSide == 1) {
                graphics.setColor(Color.green.darker());
            }
            graphics.fillRect(i, height / 2 - objectHeight / 2, 1, objectHeight);
            graphics.drawString(this.position.toString(), 0, 50);
        }
        //BufferedImage bufferedImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        //final int[] buffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        //System.arraycopy(image, 0, buffer, 0, image.length);
        //graphics.drawImage(bufferedImage, 0, 0, null);
    }

    //TODO: kept because of block height multiplier
    /*
    {
        float[][] rays = camera.castRaysOld();
        for(int i = 0; i < rays.length; i++) {
            float ray = rays[i][0];
            float side = rays[i][1];
            int mapX = (int)rays[i][2];
            int mapY = (int)rays[i][3];
            int height = 480;
            int objectHeight = (int)(height / ray);

            Color c;
            int blockHeightMultiplier = 1;
            switch (map[mapX][mapY]) {
                case 1:
                    c = new Color(190, 40, 0);
                    break;
                case 2:
                    c = new Color(40, 190, 0);
                    break;
                case 3:
                    c = new Color(0, 80, 170);
                    break;
                case 4:
                    c = new Color(190, 190, 40);
                    blockHeightMultiplier = 2;
                    break;
                default:
                    c = new Color(150, 150, 150);
                    break;
            }
            if(side == 1) {
                c = c.darker();
            }
            graphics.setColor(c);
            graphics.fillRect(i, height / 2 - objectHeight * blockHeightMultiplier / 2, 1, (int) (objectHeight * ((blockHeightMultiplier != 1) ? 3f/2f : 1f)));
        }
    }
     */

    @Deprecated(forRemoval = true)
    public float[][] castRaysOld() {
        float[][] results = new float[rayCount][2];
        for(int i = 0; i < rayCount; i++) {
            //results[i] = this.rays.get(i).cast(getPosition(), getDirection(), getPlane(), GameWorld.map, rayCount, i);
        }
        return results;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public Vector2 getPlane() {
        return plane;
    }

}
