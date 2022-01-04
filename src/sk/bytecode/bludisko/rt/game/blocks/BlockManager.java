package sk.bytecode.bludisko.rt.game.blocks;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.blocks.technical.Air;
import sk.bytecode.bludisko.rt.game.blocks.technical.MissingBlock;
import sk.bytecode.bludisko.rt.game.blocks.technical.SideWall;
import sk.bytecode.bludisko.rt.game.blocks.technical.TunnelWall;
import sk.bytecode.bludisko.rt.game.blocks.walls.BlackTiles;
import sk.bytecode.bludisko.rt.game.blocks.walls.WhiteTiles;
import sk.bytecode.bludisko.rt.game.graphics.Side;

public class BlockManager {

    private final static Block MISSING = new MissingBlock();
    private final static Block AIR = new Air();

    private final static Block WALL_OUTER_WHITE = new WhiteTiles();
    private final static Block WALL_OUTER_BLACK = new BlackTiles();

    private final static Block WALL_3 = new TunnelWall(Side.EASTWEST);
    private final static Block WALL_4 = new TunnelWall(Side.NORTHSOUTH);
    private final static Block WALL_5 = new SideWall(Side.NORTH);
    private final static Block WALL_6 = new SideWall(Side.WEST);
    private final static Block WALL_7 = new SideWall(Side.EAST);
    private final static Block WALL_8 = new SideWall(Side.SOUTH);

    @NotNull
    public static Block getBlock(int id) {
        return switch(id) {
            default -> MISSING;
            case 0 -> AIR;
            case 1 -> WALL_OUTER_WHITE;
            case 2 -> WALL_OUTER_BLACK;
            case 3 -> WALL_3;
            case 4 -> WALL_4;
            case 5 -> WALL_5;
            case 6 -> WALL_6;
            case 7 -> WALL_7;
            case 8 -> WALL_8;
        };
    }
}
