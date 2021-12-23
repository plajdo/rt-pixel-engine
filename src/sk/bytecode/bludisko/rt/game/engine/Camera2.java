package sk.bytecode.bludisko.rt.game.engine;

import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Camera2 {

    public Vector2 position; //TODO: better access control
    public Vector2 direction;
    public Vector2 plane;

    public Vector2 movementVector = Vector2.Zero;
    public float speed = 1.5f;

    private final int rayCount;
    private final List<Ray> rays;

    // plane center = position + direction
    // plane right end = pos + dir + plane
    // plane left end = pos + dir - plane

    // fov = length(dir) : length(plane)

    public Camera2(Vector2 position, Vector2 direction, float fov, int rayCount) {
        this.position = position;
        this.direction = direction;
        this.setFieldOfView(fov);

        this.rayCount = rayCount;
        this.rays = new ArrayList<>(rayCount);
        this.createRays();
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
        float oldx = direction.x;
        direction.x = (float) (direction.x * Math.cos(MathUtils.degreesToRadians * angle) - direction.y * Math.sin(MathUtils.degreesToRadians * angle));
        direction.y = (float) (oldx * Math.sin(MathUtils.degreesToRadians * angle) + direction.y * Math.cos(MathUtils.degreesToRadians * angle));

        float oldplanex = plane.x;
        plane.x = (float) (plane.x * Math.cos(MathUtils.degreesToRadians * angle) - plane.y * Math.sin(MathUtils.degreesToRadians * angle));
        plane.y = (float) (oldplanex * Math.sin(MathUtils.degreesToRadians * angle) + plane.y * Math.cos(MathUtils.degreesToRadians * angle));
    }

    public void tick(float dt) {
        position.add(movementVector.cpy().nor().scl(dt).scl(speed).rotateRad(direction.angleRad()));
    }

    public void draw(Graphics graphics) {
        castRays();

        for(int i = 0, raysSize = rays.size(); i < raysSize; i++) {
            Ray ray = rays.get(i);
            float distance = ray.getDistance();
            int hitSide = ray.getHitSide();

            int hitX = ray.getHitX();
            int hitY = ray.getHitY();

            int height = 480; // TODO: FIX
            int objectHeight = (int) (height / distance);

            graphics.setColor(Color.green);
            if(hitSide == 1) {
                graphics.setColor(Color.green.darker());
            }
            graphics.fillRect(i, height / 2 - objectHeight / 2, 1, objectHeight);
        }
    }

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

    private void createRays() {
        for(int i = 0; i < rayCount; i++) {
            rays.add(new Ray());
        }
    }

    private void castRays() {
        for(int i = 0; i < rays.size(); i++) {
            Ray ray = rays.get(i);
            ray.cast(this.position, this.direction, this.plane, GameWorld.map, rayCount, i);
        }
    }

    @Deprecated
    public float[][] castRaysOld() {
        float[][] results = new float[rayCount][2];
        for(int i = 0; i < rayCount; i++) {
            results[i] = this.rays.get(i).cast(position, direction, plane, GameWorld.map, rayCount, i);
        }
        return results;
    }

}
