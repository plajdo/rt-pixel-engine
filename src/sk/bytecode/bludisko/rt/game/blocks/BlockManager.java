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
        var coordinates = new Vector2(x, y);
        return switch(id) {
            default -> new MissingBlock(coordinates);
            case 0 -> new Air(coordinates);

            case 1 -> new WhiteTiles(coordinates);
            case 2 -> new BlackTiles(coordinates);

            case 3 -> new GlassPane(Side.SOUTH, coordinates);
            case 4 -> new GlassPane(Side.WEST, coordinates);
            case 5 -> new GlassPane(Side.NORTH, coordinates);
            case 6 -> new GlassPane(Side.EAST, coordinates);

            case 9 -> new WallWindow(coordinates, false);
            case 10 -> new WallWindow(coordinates, true);
            case 11 -> new Board(coordinates, 0);

            case 12 -> new FloorTiles(coordinates);

            case 13 -> new Doors(coordinates);
        };
    }

}
