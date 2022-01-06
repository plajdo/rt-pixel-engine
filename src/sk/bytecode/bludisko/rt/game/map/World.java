package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.entities.Player;

import java.lang.ref.WeakReference;

public abstract class World {

    protected GameMap map;
    protected WeakReference<Player> playerRef;

    public abstract void tick(float dt);

    public GameMap getMap() {
        return map;
    }

    public void setPlayer(Player playerRef) {
        this.playerRef = new WeakReference<>(playerRef);
    }

}
