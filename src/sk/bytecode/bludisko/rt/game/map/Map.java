package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.blocks.BlockManager;
import sk.bytecode.bludisko.rt.game.blocks.technical.Air;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.serialization.Serializable;

public final class Map {

    @Serializable
    private final Integer[][] tiles;
    private Block[][] blocks;

    @Deprecated
    public Map(Integer[][] tiles) {
        this.tiles = tiles;
        this.blocks = null;
    }

    @Deprecated
    public Map(int heightX, int widthY) {
        tiles = new Integer[heightX][widthY];
        this.blocks = new Block[heightX][widthY];
    }

    public void generateObjects() {
        int heightX = tiles.length;
        int widthY = tiles[0].length;

        Block[][] blocks = new Block[heightX][widthY];
        for(int x = 0; x < heightX; x++) {
            for(int y = 0; y < widthY; y++) {
                var tile = tiles[x][y];
                blocks[x][y] = BlockManager.createBlock(tile, x, y);
            }
        }
        this.blocks = blocks;
    }

    public int getWidth() {
        return blocks[0].length;
    }

    public int getHeight() {
        return blocks.length;
    }

    public Block getBlock(int x, int y) {
        if(x < 0 || y < 0) {
            return new Air(new Vector2(x, y));
        }
        if(x >= getHeight() || y >= getWidth()) {
            return new Air(new Vector2(x, y));
        }
        return blocks[x][y];
    }

    public Block[] getBlocksAt(Vector2 position) {
        boolean onEdge = coordinatesOnBlockEdge(position);

        Block centerBlock = getBlock((int) position.x, (int) position.y);
        if(!onEdge) {
            return new Block[] { centerBlock };
        }
        if(position.x % 1 == 0) {
            Block neighbouringBlockX = getBlock((int) position.x - 1, (int) position.y);
            return new Block[] { centerBlock, neighbouringBlockX };
        } else {
            Block neighbouringBlockY = getBlock((int) position.x, (int) position.y - 1);
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
