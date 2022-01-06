package sk.bytecode.bludisko.rt.game.blocks.walls;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class PortalWall extends Block {

    private final Vector2 coordinates;

    public PortalWall(Vector2 coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Texture getTexture(float side) {
        return TextureManager.getTexture(6);
    }

    @Override
    public float getHeight() {
        return 12f;
    }

    @Override
    public boolean hasPriority() {
        return false;
    }

    @Override
    public Vector2 getCoordinates() {
        return coordinates;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        var angle = ray.getDirection().angleRad();
        ray.getDirection().rotateRad((float) Math.PI + angle);

        return RayAction.ADD;
    }

}
