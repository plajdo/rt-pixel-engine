package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.blocks.technical.Air;
import sk.bytecode.bludisko.rt.game.blocks.technical.MissingBlock;
import sk.bytecode.bludisko.rt.game.blocks.technical.SideWall;
import sk.bytecode.bludisko.rt.game.blocks.walls.BlackTiles;
import sk.bytecode.bludisko.rt.game.blocks.walls.Portal;
import sk.bytecode.bludisko.rt.game.blocks.walls.SmallWhiteTiles;
import sk.bytecode.bludisko.rt.game.blocks.walls.WhiteTiles;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class BlockManager {

    public static Block createBlock(int id, int x, int y) {
        var coordinates = new Vector2(x, y);
        return switch(id) {
            default -> new MissingBlock(coordinates);
            case 0 -> new Air(coordinates);

            case 1 -> new WhiteTiles(coordinates);
            case 2 -> new BlackTiles(coordinates);

            case 3 -> new SmallWhiteTiles(coordinates);

            case 10 -> new Portal(Side.SOUTH, Portal.Color.BLUE, null, coordinates);
            case 11 -> new Portal(Side.NORTH, Portal.Color.ORANGE, null, coordinates);
            case 12 -> new Portal(Side.EAST, Portal.Color.BLUE, null, coordinates);
            case 13 -> new Portal(Side.WEST, Portal.Color.ORANGE, null, coordinates);
        };
    }
}
