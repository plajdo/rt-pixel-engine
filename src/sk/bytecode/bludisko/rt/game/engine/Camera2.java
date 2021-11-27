package sk.bytecode.bludisko.rt.game.engine;

import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.world.GameWorld;

public class Camera2 {

    public Vector2 position;
    public Vector2 direction;
    public Vector2 plane;

    private int rayCount;
    private Ray[] rays;

    // plane center = position + direction
    // plane right end = pos + dir + plane
    // plane left end = pos + dir - plane

    // fov = length(dir) : length(plane)

    public Camera2(Vector2 position, Vector2 direction, float fov, int rayCount) {
        this.position = position;
        this.direction = direction;
        this.setFieldOfView(fov);

        this.rayCount = rayCount;
        this.rays = new Ray[rayCount];
        this.createRays();

    }

    public void setFieldOfView(float degrees) {
        float halfFOV = degrees * 0.5f * MathUtils.degreesToRadians;
        float halfPlaneLength = MathUtils.sin(halfFOV) / MathUtils.sin(MathUtils.PI - halfFOV);

        //this.plane = new Vector2(0f, halfPlaneLength)
        //        .rotateRad(direction.angleRad());

        this.plane = new Vector2(0f, 0.66f);

    }

    private void createRays() {
        for(int i = 0; i < rayCount; i++) {
            this.rays[i] = new Ray();
        }
    }

    public float[][] castRays() {
        float[][] results = new float[rayCount][2];
        for(int i = 0; i < rayCount; i++) {
            results[i] = this.rays[i].cast(position, direction, plane, GameWorld.map, rayCount, i);
        }

        return results;
    }

}
