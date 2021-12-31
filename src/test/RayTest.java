package test;

import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class RayTest {

    @Test
    void rayTest() {

        Vector2 position = new Vector2(0, 0);

        Vector2 direction = new Vector2(0.316f, 0.949f);
        //Vector2 direction = new Vector2(-0.707f, 0.707f);
        //Vector2 direction = new Vector2(-0.894f, -0.447f);
        //Vector2 direction = new Vector2(0.371f, 0.928f);
        //Vector2 direction = new Vector2(0.743f, 0.669f);
        //Vector2 direction = new Vector2(0f, 1f);
        //Vector2 direction = new Vector2(0.928f, -0.371f);

        var yTileDistance = Math.abs(direction.y / direction.x);
        var xTileDistance = Math.abs(1 / yTileDistance);

        int xSign = (int) Math.signum(direction.x);
        int ySign = (int) Math.signum(direction.y);

        System.out.println(yTileDistance);
        System.out.println(xTileDistance);

        System.out.println(xSign);
        System.out.println(ySign);

        int side = 0;
        boolean hit = false;

        while(!hit) {
            if(xTileDistance < yTileDistance) {

                float nextBlockDistanceX = (int) position.x - position.x + 1f;
                float nextBlockDistanceY = (int) position.y - position.y + 1f;

                if(nextBlockDistanceX < xTileDistance) {
                    position.add(nextBlockDistanceX * xSign, yTileDistance * nextBlockDistanceX * ySign);
                } else {
                    position.add(xTileDistance * nextBlockDistanceY * xSign, nextBlockDistanceY * ySign);
                }

            } else {
                float nextBlockDistanceX = (float) (Math.ceil(position.x) - position.x);
                float nextBlockDistanceY = (float) (Math.ceil(position.y) - position.y);

                float dist1 = new Vector2(nextBlockDistanceX, nextBlockDistanceX * yTileDistance).len2();
                float dist2 = new Vector2(nextBlockDistanceY / yTileDistance, nextBlockDistanceY).len2();

                if(dist1 < dist2) {
                    position.add(nextBlockDistanceX, yTileDistance * Math.abs(nextBlockDistanceX) * ySign);
                } else {
                    position.add(xTileDistance * Math.abs(nextBlockDistanceY) * xSign, nextBlockDistanceY);
                }

            }

            //position.add(new Vector2(xSign, yTileDistance * ySign));

            System.out.println(position);
        }

    }

}
