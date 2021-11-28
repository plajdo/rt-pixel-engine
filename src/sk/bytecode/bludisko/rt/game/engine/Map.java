package sk.bytecode.bludisko.rt.game.engine;

import sk.bytecode.bludisko.rt.game.engine.serialization.Serializable;

public class Map {

    @Serializable
    private int[][] tiles;

    public Map() {
        this.tiles = new int[][]{{2, 3, 4}, {5, 6, 7}, {0, 3, 5}};
    }

}
