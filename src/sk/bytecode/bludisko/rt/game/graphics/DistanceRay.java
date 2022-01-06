package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class DistanceRay extends Ray {

    // MARK: - Constructor

    public DistanceRay(Map map, Vector2 position, Vector2 direction) {
        super(map, position, direction);
    }

    // MARK: - Public

    public float cast(float maxLength) {
        step();
        if(distance >= maxLength) {
            return -1;
        }
        while(distance < maxLength) {
            Block[] blocks = map.getBlocksAt(position);
            for(Block block : blocks) {
                RayAction hit = block.hitAction(this);
                if(hit == RayAction.SKIP) {
                    continue;
                } else if(hit == RayAction.TELEPORT) {
                    return Float.NaN;
                }
                return distance;
            }
            step();
        }

        return -1;
    }

}
