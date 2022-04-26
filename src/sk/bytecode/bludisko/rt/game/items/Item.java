package sk.bytecode.bludisko.rt.game.items;

import sk.bytecode.bludisko.rt.game.graphics.Texture;

public abstract class Item {

    // MARK: - Attributes

    protected Texture overlay;

    // MARK: - Public

    public Texture getOverlay() {
        return overlay;
    }

}
