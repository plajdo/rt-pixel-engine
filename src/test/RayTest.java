package test;

import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class RayTest {

    @Test
    void rayVectorTest() {

        //Vector2 position = new Vector2(2f, -2.235698f);
        Vector2 position = new Vector2(2f, 2f);
        //Vector2 position = new Vector2(4.0014944f, 4.7758474f);

        //Vector2 direction = new Vector2(0.31623f, 0.94869f);           //   I. kv.
        //Vector2 direction = new Vector2(-0.31623f, 0.94869f);          //  II. kv.
        //Vector2 direction = new Vector2(-0.31623f, -0.94869f);         // III. kv.
        //Vector2 direction = new Vector2(0.31623f, -0.94869f);          //  IV. kv.

        //Vector2 direction = new Vector2(0.958f, 0.287f);           //   I. kv.
        //Vector2 direction = new Vector2(-0.958f, 0.287f);          //  II. kv.
        //Vector2 direction = new Vector2(-0.958f, -0.287f);         // III. kv.
        //Vector2 direction = new Vector2(0.958f, -0.287f);          //  IV. kv.

        //Vector2 direction = new Vector2(-0.707f, 0.707f);
        //Vector2 direction = new Vector2(-0.894f, -0.447f);
        //Vector2 direction = new Vector2(0.371f, 0.928f);
        //Vector2 direction = new Vector2(0.743f, 0.669f);
        //Vector2 direction = new Vector2(0f, 1f);
        Vector2 direction = new Vector2(0.928f, -0.371f);

        //Vector2 direction = new Vector2(0.76250285f, -0.6469849f);

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
            Vector2 positionInTile = new Vector2(
                    position.x - (int) position.x,
                    position.y - (int) position.y
            );

            Vector2 nextTileDistance = new Vector2(1, 1)
                    .sub(positionInTile)
                    .sub(
                            Float.floatToRawIntBits(positionInTile.x) >>> 31,
                            Float.floatToRawIntBits(positionInTile.y) >>> 31
                    ).scl(sign);

            // Fix to account for float precision
            if(Math.abs(nextTileDistance.x) < MathUtils.FLOAT_ROUNDING_ERROR * 10) {
                position.x = Math.round(position.x);
                continue;
            }
            if(Math.abs(nextTileDistance.y) < MathUtils.FLOAT_ROUNDING_ERROR * 10) {
                position.y = Math.round(position.y);
                continue;
            }

            Vector2 nextStepDistance = nextTileDistance.cpy()
                    //.scl(tileDistance).scl(sign);
                    .scl(
                            (float) (1 / Math.sin((Math.PI / 2) - direction.angleRad())),
                            (float) (1 / Math.sin((Math.PI / 2) - ((Math.PI / 2) - direction.angleRad())))
                    );

            // c/sin(90) = b/sin(180 - 90 - A)
            // Vector2 nextTileDistance = sign.cpy().scl(-1).sub(1, 1).scl(-0.5f).sub(positionInTile);

            if(nextStepDistance.x < nextStepDistance.y) {
                position.add(nextTileDistance.x, tileDistance.x * nextTileDistance.x * sign.x);
            } else {
                position.add(tileDistance.y * nextTileDistance.y * sign.y, nextTileDistance.y);
            }

            System.out.println(position);
        }

    }

}
