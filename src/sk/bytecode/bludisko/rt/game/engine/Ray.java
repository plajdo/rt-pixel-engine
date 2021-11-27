package sk.bytecode.bludisko.rt.game.engine;

import sk.bytecode.bludisko.rt.game.math.Vector2;

public final class Ray {

    private Vector2 rayDir;

    public float[] cast(Vector2 position, Vector2 direction, Vector2 plane, int[][] map, int rayCount, int x) {

        // MARK: - Setup

        float screenX = 2f * x / rayCount - 1f;
        this.rayDir = new Vector2(
                direction.x + plane.x * screenX,
                direction.y + plane.y * screenX
        );

        int mapX = (int)position.x;
        int mapY = (int)position.y;

        Vector2 squareSideDistance = new Vector2();
        Vector2 squareSideDifference = new Vector2();
        squareSideDifference.x = Math.abs(1 / rayDir.x);
        squareSideDifference.y = Math.abs(1 / rayDir.y);

        //Vector2 squareSideDifference = new Vector2(rayDir.len() / rayDir.x, rayDir.len() / rayDir.y);
        //Vector2 nextSqSD = rayDir.cpy().scl(rayDir.len());
        //assert squareSideDifference.equals(nextSqSD);

        Vector2 step = new Vector2();

        boolean hit = false;
        int side = 0;

        // MARK: - First step

        if(rayDir.x < 0f) {
            step.x = -1;
            squareSideDistance.x = (position.x - mapX) * squareSideDifference.x;
        } else {
            step.x = 1;
            squareSideDistance.x = (mapX + 1f - position.x) * squareSideDifference.x;
        }

        if(rayDir.y < 0f) {
            step.y = -1;
            squareSideDistance.y = (position.y - mapY) * squareSideDifference.y;
        } else {
            step.y = 1;
            squareSideDistance.y = (mapY + 1f - position.y) * squareSideDifference.y;
        }

        // MARK: - Next wall hit

        while(!hit) {
            if(squareSideDistance.x < squareSideDistance.y) {
                squareSideDistance.x += squareSideDifference.x;
                mapX += step.x;
                side = 0;

            } else {
                squareSideDistance.y += squareSideDifference.y;
                mapY += step.y;
                side = 1;

            }
            if(map[mapX][mapY] > 0) hit = true;

        }

        // MARK: - Distance

        float perpendicularSideDistance;
        if(side == 0) {
            perpendicularSideDistance = squareSideDistance.x - squareSideDifference.x;
        } else {
            perpendicularSideDistance = squareSideDistance.y - squareSideDifference.y;
        }

        return new float[] {perpendicularSideDistance, side, mapX, mapY};
    }

}
