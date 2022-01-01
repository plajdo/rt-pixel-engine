package sk.bytecode.bludisko.rt.game.blocks;

public class BlockManager {

    public static Block getBlock(int id) {
        if(id == 0) {
            return null;
        }
        return new FullBlock();
    }

}
