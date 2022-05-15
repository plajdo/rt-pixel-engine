package sk.bytecode.bludisko.rt.game.graphics;

public interface Tickable {

    /**
     * Game loop - logic.
     * Called every frame before rendering. Should contain
     * logic that's used to update information for renderer to draw
     * a new frame.
     * @param dt Time difference between the last and this frame (delta time)
     */
    void tick(float dt);

}
