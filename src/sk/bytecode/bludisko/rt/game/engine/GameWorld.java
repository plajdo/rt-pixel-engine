package sk.bytecode.bludisko.rt.game.engine;

import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.awt.*;

public final class GameWorld {

    public static int[][] map =
    {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,2,2,2,2,2,0,0,0,0,3,0,3,0,3,0,0,0,1},
        {1,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,2,0,0,0,2,0,0,0,0,3,0,0,0,3,0,0,0,1},
        {1,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,2,2,0,2,2,0,0,0,0,3,0,3,0,3,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,4,0,4,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,4,0,0,0,0,5,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,4,0,4,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,4,0,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,4,4,4,4,4,4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    private Camera2 camera;

    public GameWorld() {
        Vector2 cameraPosition = new Vector2(22, 12);
        Vector2 cameraDirection = new Vector2(-1, 0);
        float fieldOfView = 66f;

        this.camera = new Camera2(cameraPosition, cameraDirection, fieldOfView, 640);

        new MapManager("no");

    }

    public void draw(Graphics graphics) {
        float[][] rays = camera.castRays();
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
            graphics.fillRect(i, height / 2 - objectHeight * blockHeightMultiplier / 2, 1, objectHeight * blockHeightMultiplier);

        }

    }

    public void tick(float dt) {
        float oldx = camera.direction.x;
        camera.direction.x = (float) (camera.direction.x * Math.cos(-0.01f) - camera.direction.y * Math.sin(-0.01f));
        camera.direction.y = (float) (oldx * Math.sin(-0.01f) + camera.direction.y * Math.cos(-0.01f));

        float oldplanex = camera.plane.x;
        camera.plane.x = (float) (camera.plane.x * Math.cos(-0.01f) - camera.plane.y * Math.sin(-0.01f));
        camera.plane.y = (float) (oldplanex * Math.sin(-0.01f) + camera.plane.y * Math.cos(-0.01f));

    }

}
