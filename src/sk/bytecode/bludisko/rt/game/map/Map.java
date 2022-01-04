package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.blocks.BlockManager;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.serialization.Serializable;

public class Map {

    @Serializable
    private final Integer[][] tiles;

    public Map(Integer[][] tiles) {
        this.tiles = tiles;
    }

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

    public void setTile(int x, int y, int value) {
        //TODO: proper check
        tiles[x][y] = value;
    }

    public Block[] getBlocksAt(Vector2 position) {
        boolean onEdge = coordinatesOnBlockEdge(position);

        Block centerBlock = BlockManager.getBlock(getTile((int) position.x, (int) position.y));
        if(!onEdge) {
            return new Block[] { centerBlock };
        }
        if(position.x % 1 == 0) {
            Block neighbouringBlockX = BlockManager.getBlock(getTile((int) position.x - 1, (int) position.y));
            return new Block[] { centerBlock, neighbouringBlockX };
        } else {
            Block neighbouringBlockY = BlockManager.getBlock(getTile((int) position.x, (int) position.y - 1));
            return new Block[] { centerBlock, neighbouringBlockY };
        }
    }

    private boolean coordinatesOnBlockEdge(Vector2 position) {
        Vector2 positionInBlockCoords = MathUtils.decimalPart(position);
        return positionInBlockCoords.x == 0 ||
               positionInBlockCoords.y == 0 ||
               positionInBlockCoords.x == 1 ||
               positionInBlockCoords.y == 1;
    }

}
