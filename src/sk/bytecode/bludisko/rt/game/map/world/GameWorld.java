package sk.bytecode.bludisko.rt.game.map.world;

import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.map.GameMap;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.awt.*;

public final class GameWorld {

    private Camera camera;
    private GameMap map;

    // MARK: - Constructor

    public GameWorld() {
        setupMap();
        setupCamera();
    }

    private void setupMap() {
        this.map = new GameMap("testMap2", 0, 0);

    }

    @Deprecated(forRemoval = true)
    public Camera getCamera() {
        return camera;
    }

    // MARK: - Game loop

    public void draw(Graphics graphics) {
        camera.draw(graphics);
    }
/*
    @Deprecated(forRemoval = true) // NOTE: has block height code down, kept for knowledge reasons
    public void drawOld(Graphics graphics) {
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
    }*/

    public void tick(float dt) {
        camera.tick(dt);

        //float oldx = camera.direction.x;
        //camera.direction.x = (float) (camera.direction.x * Math.cos(-0.015f) - camera.direction.y * Math.sin(-0.015f));
        //camera.direction.y = (float) (oldx * Math.sin(-0.015f) + camera.direction.y * Math.cos(-0.015f));

        //float oldplanex = camera.plane.x;
        //camera.plane.x = (float) (camera.plane.x * Math.cos(-0.015f) - camera.plane.y * Math.sin(-0.015f));
        //camera.plane.y = (float) (oldplanex * Math.sin(-0.015f) + camera.plane.y * Math.cos(-0.015f));

    }

    // MARK: - Private

    private void setupCamera() {
        Vector2 cameraPosition = new Vector2(6.5f, 0f);
        Vector2 cameraDirection = new Vector2(0, 1);
        float fieldOfView = 66f;

        this.camera = new Camera(map.getWallMap(), cameraPosition, cameraDirection, fieldOfView, 640);

    }

}
