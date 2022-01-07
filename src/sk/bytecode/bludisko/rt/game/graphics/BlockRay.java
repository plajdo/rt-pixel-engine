package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.util.ArrayList;

/**
 * Extension of the Ray that can be stepped in a map until passing the
 * maximum number of steps or getting a STOP action from the Block it had hit.
 */
public class BlockRay extends Ray {

    private final Map map;

    // MARK: - Constructor

    /**
     * Constructs a new BlockRay in a given map.
     * @param map Map to use
     * @param position Starting position
     * @param direction Starting direction
     */
    public BlockRay(Map map, Vector2 position, Vector2 direction) {
        super(position, direction);
        this.map = map;
    }

    // MARK: - Public

    /**
     * Steps the ray in a map for the given maximum number of steps, or
     * until getting a STOP signal from any of the blocks it had hit.
     * @param maxSteps Maximum number of steps to take
     * @return ArrayList with all hits the ray had made during the stepping.
     * Contains hits of all Blocks that signaled that they are translucent (ADD),
     * portals (TELEPORT) or solids (STOP).
     * @see RayAction
     * @see Ray
     * @see Block
     */
    public ArrayList<RayHit<Block>> cast(int maxSteps) {
        ArrayList<RayHit<Block>> hits = new ArrayList<>();
        castCycle: for(int i = 0; i < maxSteps; i++) {
            step();

            Block[] blocks = map.getBlocksAt(position);

            for(Block block : blocks) {
                Vector2 rayPosition = this.position.cpy();
                RayAction action = block.hitAction(this);

                switch(action) {
                    case ADD:
                        hits.add(new RayHit<>(block, this.position.cpy(), distance));
                        break;
                    case TELEPORT:
                        hits.add(new RayHit<>(block, rayPosition, distance));
                        break;
                    case STOP:
                        hits.add(new RayHit<>(block, this.position.cpy(), distance));
                        break castCycle;
                }
            }
        }

        return hits;
    }

}
