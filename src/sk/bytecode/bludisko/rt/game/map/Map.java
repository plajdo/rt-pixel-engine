package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.blocks.BlockManager;
import sk.bytecode.bludisko.rt.game.serialization.Serializable;

public class Map {

    @Serializable
    private final Integer[][] tiles;

    public Map(int heightX, int widthY) {
        tiles = new Integer[heightX][widthY];
    }

    public int getWidth() {
        return tiles[0].length;
    }

    public int getHeight() {
        return tiles.length;
    }

    public int getTile(int x, int y) {
        if(x < 0 || y < 0) {
            return 0;
        }
        if(x >= getHeight() || y >= getWidth()) {
            return 0;
        }
        var value = tiles[x][y];
        return value == null ? 0 : value;
    }

    public Block getBlock(int x, int y) {
        return BlockManager.getBlock(getTile(x, y));
    }

}
