package test;

import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class RayTest {

    @Test
    void rayVectorTest() {

        //Vector2 position = new Vector2(1.2f, -1.56f);
        //Vector2 position = new Vector2(2f, -2.235698f);
        //Vector2 position = new Vector2(2f, 2f);
        //Vector2 position = new Vector2(4.0014944f, 4.7758474f);
        //Vector2 position = new Vector2(1.5f, 0.5f);
        Vector2 position = new Vector2(-2.36f, 0.32f);

        //Vector2 direction = new Vector2(0.31623f, 0.94869f);           //   I. kv.
        //Vector2 direction = new Vector2(-0.31623f, 0.94869f);          //  II. kv.
        //Vector2 direction = new Vector2(-0.31623f, -0.94869f);         // III. kv.
        //Vector2 direction = new Vector2(0.31623f, -0.94869f);          //  IV. kv.

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

        //Vector2 direction = new Vector2(0.76250285f, -0.6469849f);

        Vector2 tileDistance = new Vector2(
                Math.abs(direction.y / direction.x),
                Math.abs(direction.x / direction.y)
        );

        Vector2 sign = new Vector2(
                Math.copySign(1f, direction.x),
                Math.copySign(1f, direction.y)
        );

        Vector2 rawSign = new Vector2(
                Float.floatToRawIntBits(direction.x) >>> 31,
                Float.floatToRawIntBits(direction.y) >>> 31
        );

        System.out.println(tileDistance.x);
        System.out.println(tileDistance.y);

        System.out.println(sign.x);
        System.out.println(sign.y);

        int side = 0;
        boolean hit = false;

        while(!hit) {
            Vector2 positionInTile = new Vector2(
                    (float) Math.abs(Math.floor(position.x) - position.x),
                    (float) Math.abs(Math.floor(position.y) - position.y)
            );

            Vector2 nextTileDistance;
            if(sign.x > 0 && sign.y > 0) {
                nextTileDistance = new Vector2(1, 1).sub(positionInTile); // TODO: optimalizovať ify

            } else if(sign.x > 0 && sign.y < 0) {
                nextTileDistance = new Vector2(1 - positionInTile.x, 0 + positionInTile.y);

            } else if(sign.x < 0 && sign.y > 0) {
                nextTileDistance = new Vector2(0 + positionInTile.x, 1 - positionInTile.y);

            } else {
                nextTileDistance = new Vector2(0 + positionInTile.x, 0 + positionInTile.y);
            }

            if(nextTileDistance.x == 0) {
                nextTileDistance.x = 1;
            }
            if(nextTileDistance.y == 0) {
                nextTileDistance.y = 1;
            }

            Vector2 nextStepDistance = nextTileDistance.cpy()
                    .scl(
                            Math.abs((float) (1 / Math.sin((Math.PI / 2) - direction.angleRad()))),
                            Math.abs((float) (1 / Math.sin((Math.PI / 2) - ((Math.PI / 2) - direction.angleRad()))))
                    );

            // c/sin(90) = b/sin(180 - 90 - A)
            // Vector2 nextTileDistance = sign.cpy().scl(-1).sub(1, 1).scl(-0.5f).sub(positionInTile);

            Vector2 step = tileDistance.cpy().scl(nextTileDistance);

            if(nextStepDistance.x < nextStepDistance.y) {
                position.add(nextTileDistance.x * sign.x, tileDistance.x * nextTileDistance.x * sign.y);
            } else {
                position.add(tileDistance.y * nextTileDistance.y * sign.x, nextTileDistance.y  * sign.y);
            }

            System.out.println(position);
        }

    }

}
