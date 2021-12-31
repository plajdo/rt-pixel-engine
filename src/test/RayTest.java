package test;

import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class RayTest {

    @Test
    void rayTest() {

        Vector2 position = new Vector2(0, 0);

        //Vector2 direction = new Vector2(0.316f, 0.949f);           //   I. kv.
        //Vector2 direction = new Vector2(-0.316f, 0.949f);          //  II. kv.
        //Vector2 direction = new Vector2(-0.316f, -0.949f);         // III. kv.
        //Vector2 direction = new Vector2(0.316f, -0.949f);          //  IV. kv.

        //Vector2 direction = new Vector2(0.958f, 0.287f);           //   I. kv.
        //Vector2 direction = new Vector2(-0.958f, 0.287f);          //  II. kv.
        //Vector2 direction = new Vector2(-0.958f, -0.287f);         // III. kv.
        Vector2 direction = new Vector2(0.958f, -0.287f);          //  IV. kv.

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
            float nextBlockDistanceX = (int) position.x - position.x + (1f * xSign);
            float nextBlockDistanceY = (int) position.y - position.y + (1f * ySign);

            float dist1 = new Vector2(nextBlockDistanceX, nextBlockDistanceX * yTileDistance).len2();
            float dist2 = new Vector2(nextBlockDistanceY / yTileDistance, nextBlockDistanceY).len2();

            if(dist1 < dist2) {
                position.add(nextBlockDistanceX, yTileDistance * Math.abs(nextBlockDistanceX) * ySign);
            } else {
                position.add(xTileDistance * Math.abs(nextBlockDistanceY) * xSign, nextBlockDistanceY);
            }

            //position.add(new Vector2(xSign, yTileDistance * ySign));

            System.out.println(position);
        }

    }

    @Test
    void rayVectorTest() {

        Vector2 position = new Vector2(2, -2);

        //Vector2 direction = new Vector2(0.316f, 0.949f);           //   I. kv.
        //Vector2 direction = new Vector2(-0.316f, 0.949f);          //  II. kv.
        //Vector2 direction = new Vector2(-0.316f, -0.949f);         // III. kv.
        //Vector2 direction = new Vector2(0.316f, -0.949f);          //  IV. kv.

        //Vector2 direction = new Vector2(0.958f, 0.287f);           //   I. kv.
        //Vector2 direction = new Vector2(-0.958f, 0.287f);          //  II. kv.
        //Vector2 direction = new Vector2(-0.958f, -0.287f);         // III. kv.
        //Vector2 direction = new Vector2(0.958f, -0.287f);          //  IV. kv.

        //Vector2 direction = new Vector2(-0.707f, 0.707f);
        //Vector2 direction = new Vector2(-0.894f, -0.447f);
        //Vector2 direction = new Vector2(0.371f, 0.928f);
        //Vector2 direction = new Vector2(0.743f, 0.669f);
        Vector2 direction = new Vector2(0f, 1f);
        //Vector2 direction = new Vector2(0.928f, -0.371f);

        Vector2 tileDistance = new Vector2(
                Math.abs(direction.y / direction.x),
                Math.abs(direction.x / direction.y)
        );

        Vector2 sign = new Vector2(
                Math.copySign(1f, direction.x),
                Math.copySign(1f, direction.y)
        );

        System.out.println(tileDistance.x);
        System.out.println(tileDistance.y);

        System.out.println(sign.x);
        System.out.println(sign.y);

        int side = 0;
        boolean hit = false;

        while(!hit) {

            Vector2 nextTileDistance = new Vector2(
                    (int) position.x - position.x + (sign.x),
                    (int) position.y - position.y + (sign.y)
            );

            Vector2 nextDistance = new Vector2(
                    new Vector2(nextTileDistance.x, nextTileDistance.x * tileDistance.y).len2(),
                    new Vector2(nextTileDistance.y / tileDistance.y, nextTileDistance.y).len2()
            );

            if(nextDistance.x < nextDistance.y) {
                position.add(nextTileDistance.x, tileDistance.y * Math.abs(nextTileDistance.x) * sign.y);
            } else {
                position.add(tileDistance.x * Math.abs(nextTileDistance.y) * sign.x, nextTileDistance.y);
            }

            System.out.println(position);
        }

    }

}
