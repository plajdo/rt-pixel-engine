package sk.bytecode.bludisko.rt.game.items;

import sk.bytecode.bludisko.rt.game.graphics.TextureManager;

import java.lang.ref.WeakReference;

public class PortalGun extends Item {

    // private WeakReference<World> world;

    public PortalGun() {
        this.overlay = TextureManager.getTexture(13);
    }

    @Override
    public void use() {
        System.out.println("LMB");
    }

    @Override
    public void useSecondary() {
        System.out.println("RMB");
    }

}
