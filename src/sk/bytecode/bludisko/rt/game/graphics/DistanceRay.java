package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.blocks.BlockManager;
import sk.bytecode.bludisko.rt.game.blocks.technical.Air;
import sk.bytecode.bludisko.rt.game.blocks.walls.Portal;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class DistanceRay extends Ray {

    public DistanceRay(Map map, Vector2 position, Vector2 direction) {
        super(map, position, direction);
    }

    public float cast(float maxLength) {
        step();
        if(distance >= maxLength) {
            return -1;
        }
        while(distance < maxLength) {
            Block[] blocks = map.getBlocksAt(position);
            for(Block block : blocks) {
                if(!(block instanceof Air)) {
                    return distance;
                }
            }
            step();
        }

        return -1;
    }

}
