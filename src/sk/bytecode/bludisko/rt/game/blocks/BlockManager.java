package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.blocks.technical.Air;
import sk.bytecode.bludisko.rt.game.blocks.technical.MissingBlock;
import sk.bytecode.bludisko.rt.game.blocks.game.*;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class BlockManager {

    public static Block createBlock(int id, int x, int y) {
        var coordinates = new Vector2(x, y);
        return switch(id) {
            default -> new MissingBlock(coordinates);
            case 0 -> new Air(coordinates);

            case 1 -> new WhiteTiles(coordinates, 4);
            case 2 -> new BlackTiles(coordinates, 4);

            case 3 -> new GlassPane(Side.SOUTH, coordinates, 4);
            case 4 -> new GlassPane(Side.WEST, coordinates, 4);
            case 5 -> new GlassPane(Side.NORTH, coordinates, 4);
            case 6 -> new GlassPane(Side.EAST, coordinates, 4);

            case 7 -> new Portal(Side.EAST, Portal.Color.ORANGE, coordinates, 4);
            case 8 -> new Portal(Side.SOUTH, Portal.Color.BLUE, coordinates, 4);

            case 9 -> new WallWindow(coordinates, false);
            case 10 -> new WallWindow(coordinates, true);
            case 11 -> new Board(coordinates, 0);

            case 12 -> new FloorTiles(coordinates);

            case 13 -> new Doors(coordinates);
        };
    }
}
