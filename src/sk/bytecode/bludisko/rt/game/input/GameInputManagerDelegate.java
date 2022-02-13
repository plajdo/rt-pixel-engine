package sk.bytecode.bludisko.rt.game.input;

import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Interface used to update Input Information to delegated objects.
 * Uses delegation.
 */
public interface GameInputManagerDelegate {

    /**
     * Called when movement keys from keyboard are changed.
     * Might fire rapidly due to system-defined key repeat.
     * Set keybinds in Config to set the direction.
     * @param direction Direction the keys are pointing in
     * @see sk.bytecode.bludisko.rt.game.util.Config
     */
    void didUpdateMovementDirection(Vector2 direction);

    /**
     * Called every time the mouse has changed position.
     * @param rotation Vector2 describing the rotation
     *                 (x - horizontal pixel distance,
     *                 y - vertical pixel distance)
     */
    void didUpdateRotation(Vector2 rotation);

    /**
     * Called when the player starts sprinting.
     * Check Config to set the key.
     * @param isSprinting Whether the player is sprinting
     */
    void didUpdateSprintingStatus(boolean isSprinting);

}
