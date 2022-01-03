package sk.bytecode.bludisko.rt.game.blocks;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.blocks.technical.Air;
import sk.bytecode.bludisko.rt.game.blocks.technical.MissingBlock;
import sk.bytecode.bludisko.rt.game.blocks.walls.HalfWall;
import sk.bytecode.bludisko.rt.game.blocks.walls.RedWall;

public class BlockManager {

    private final static Block MISSING = new MissingBlock();
    private final static Block AIR = new Air();

    private final static Block WALL_1 = new RedWall();
    private final static Block WALL_2 = new HalfWall();

    @NotNull
    // TODO: remove BlockManager and save/load blocks directly instead
    public static Block getBlock(int id) {
        return switch(id) {
            default -> MISSING;
            case 0 -> AIR;
            case 1 -> WALL_1;
            case 2 -> WALL_1;
            case 3 -> WALL_2;
        };
    }

}
