package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Extension of the Ray that can be stepped in a map
 * giving the distance to the next block.
 */
public class DistanceRay extends Ray {

    private final Map map;

    // MARK: - Constructor

    /**
     * Constructs a new DistanceRay inside a map.
     * @param map Map to use to get data about Blocks.
     * @param position Starting position
     * @param direction Starting direction
     * @see DistanceRay
     * @see Ray
     * @see Map
     * @see Block
     */
    public DistanceRay(Map map, Vector2 position, Vector2 direction) {
        super(position, direction);
        this.map = map;
    }

    // MARK: - Public

    /**
     * Step the ray in a map until it reaches a block
     * or its travelled distance exceeds maximum given length.
     * @param maxLength Maximum length to travel
     * @return NaN if the ray is moved to a different location during stepping,
     * -1 if the length exceeded the maximum distance
     * or the distance it has measured until the next Block.
     */
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
