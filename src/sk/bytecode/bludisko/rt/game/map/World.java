package sk.bytecode.bludisko.rt.game.map;

import org.jetbrains.annotations.Nullable;
import sk.bytecode.bludisko.rt.game.entities.Player;

import java.lang.ref.WeakReference;

/**
 * Class that contains game logic for the given world it represents.
 * World should process game events, update animations, tick
 * tile entities, etc.
 */
public abstract class World {

    protected GameMap map;
    protected WeakReference<Player> playerRef;

    /**
     * Game loop - logic.
     * Should be called every frame before rendering a frame, probably
     * from another tick(float) method.
     * @param dt Time that's passed since the last frame finished processing
     */
    public abstract void tick(float dt);

    /**
     * @return Map of this world
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Set a weak reference to a player created by a Screen.
     * @param playerRef Reference to a player.
     */
    public void setPlayer(@Nullable Player playerRef) {
        this.playerRef = new WeakReference<>(playerRef);
    }

}
