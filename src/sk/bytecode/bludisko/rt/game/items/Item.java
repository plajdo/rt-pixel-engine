package sk.bytecode.bludisko.rt.game.items;

import sk.bytecode.bludisko.rt.game.graphics.Texture;

/**
 * Abstract class representing a common and default implementation
 * for all other in-game items.
 */
public abstract class Item {

    // MARK: - Attributes

    protected Texture overlay;

    // MARK: - Public

    /**
     * Default item use action.
     * Called after a user input, usually LMB.
     * @see sk.bytecode.bludisko.rt.game.entities.Player#didToggleMouseButton(boolean)
     * @see sk.bytecode.bludisko.rt.game.input.GameInputManagerDelegate
     */
    public void use() {}

    /**
     * Secondary item use action.
     * Called after a user input, usually RMB.
     * @see sk.bytecode.bludisko.rt.game.entities.Player#didToggleMouseButton(boolean)
     * @see sk.bytecode.bludisko.rt.game.input.GameInputManagerDelegate
     */
    public void useSecondary() {}

    /**
     * @return Texture of the item to be rendered as an overlay
     */
    public Texture getOverlay() {
        return overlay;
    }

}
