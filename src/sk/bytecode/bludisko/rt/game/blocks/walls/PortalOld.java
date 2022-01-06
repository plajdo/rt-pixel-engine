package sk.bytecode.bludisko.rt.game.blocks.walls;

import sk.bytecode.bludisko.rt.game.blocks.technical.SideWall;
import sk.bytecode.bludisko.rt.game.graphics.*;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
/*
@Deprecated(forRemoval = true)
public class Portal extends SideWall {

    public enum Color {
        BLUE,
        ORANGE
    }

    private final Portal.Color color;
    private Portal otherPortal;

    public Portal(Side side, Portal.Color color, Portal otherPortal, Vector2 position) {
        super(side, position);
        this.color = color;
        this.otherPortal = otherPortal;
    }

    @Override
    public Texture getTexture(Side side) {
        if(color == Color.BLUE) {
            return TextureManager.getTexture(2);
        }
        return TextureManager.getTexture(3);
    }

    @Override
    public RayAction hitAction(Ray ray) {
        var rayPosition = ray.getPosition();
        var positionInBlock = MathUtils.decimalPart(rayPosition);

        var hitAction = super.hitAction(ray);

        if(otherPortal != null && hitAction == RayAction.ADD) {
            var otherPortalPosition = otherPortal.getCoordinates();

            rayPosition.set(otherPortalPosition.cpy().add(positionInBlock));
            if(otherPortal.getSide() == getSide()) {
                ray.getDirection().rotate90(1).rotate90(1);
            }
        }
        return hitAction;
    }

    public void setOtherPortal(Portal otherPortal) {
        this.otherPortal = otherPortal;
    }

}*/