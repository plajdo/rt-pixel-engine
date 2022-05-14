package sk.bytecode.bludisko.rt.game.items;

import sk.bytecode.bludisko.rt.game.blocks.game.Portal;
import sk.bytecode.bludisko.rt.game.blocks.group.PortalPlaceable;
import sk.bytecode.bludisko.rt.game.entities.Player;
import sk.bytecode.bludisko.rt.game.graphics.BlockRay;
import sk.bytecode.bludisko.rt.game.graphics.RayHit;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.map.PortalManager;
import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.lang.ref.WeakReference;

public class PortalGun extends Item {

    private static final int RANGE = 100;

    private WeakReference<Player> player;

    public PortalGun(Player owner) {
        this.overlay = TextureManager.getTexture(13);
        this.player = new WeakReference<>(owner);
    }

    @Override
    public void use() {
        NullSafe.acceptWeak(player, player -> {
            var map = player.getWorld().getMap();
            var ray = new BlockRay(map.walls(), player.getPosition(), player.getDirection());
            var hitList = ray.cast(RANGE);

            hitList.stream()
                    .findFirst()
                    .map(RayHit::position)
                    .ifPresent(position -> PortalManager.createPortal(map, position, Portal.Color.ORANGE));
        });
    }

    @Override
    public void useSecondary() {
        System.out.println("RMB");
    }

}
