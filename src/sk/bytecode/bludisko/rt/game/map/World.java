package sk.bytecode.bludisko.rt.game.map;

public abstract class World {

    protected GameMap map;

    public abstract void tick(float dt);

    public GameMap getMap() {
        return map;
    }
}
