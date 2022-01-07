package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.blocks.technical.Air;
import sk.bytecode.bludisko.rt.game.blocks.technical.MissingBlock;
import sk.bytecode.bludisko.rt.game.blocks.game.*;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Helps with creating Block maps from Integer maps loaded from files.
 * Fully static class. Cannot be instantiated.
 */
public class BlockManager {

    private BlockManager(){}

    /**
     * Creates a new block with a given ID and coordinates in an integer plane.
     * @return New block
     */
    public static Block createBlock(int id, int x, int y) {
        Vector2 coordinates = new Vector2(x, y);
        switch(id) {
            default: return new MissingBlock(coordinates);
            case 0: return new Air(coordinates);

            case 1: return new WhiteTiles(coordinates);
            case 2: return new BlackTiles(coordinates);

            case 3: return new GlassPane(Side.SOUTH, coordinates);
            case 4: return new GlassPane(Side.WEST, coordinates);
            case 5: return new GlassPane(Side.NORTH, coordinates);
            case 6: return new GlassPane(Side.EAST, coordinates);

            case 7: return new Portal(Side.EAST, Portal.Color.ORANGE, coordinates);
            case 8: return new Portal(Side.SOUTH, Portal.Color.BLUE, coordinates);

            case 9: return new WallWindow(coordinates, false);
            case 10: return new WallWindow(coordinates, true);
            case 11: return new Board(coordinates, 0);

            case 12: return new FloorTiles(coordinates);

            case 13: return new Doors(coordinates);
        }
    }

}
