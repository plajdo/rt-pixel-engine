package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.util.ArrayList;

public class BlockRay extends Ray {

    // MARK: - Constructor

    public BlockRay(Map map, Vector2 position, Vector2 direction) {
        super(map, position, direction);
    }

    // MARK: - Public

    public ArrayList<RayHit<Block>> cast(int maxSteps) {
        ArrayList<RayHit<Block>> hits = new ArrayList<>();
        castCycle: for(int i = 0; i < maxSteps; i++) {
            step();

            Block[] blocks = map.getBlocksAt(position);

            for(Block block : blocks) {
                Vector2 rayPosition = this.position.cpy();
                RayAction action = block.hitAction(this);

                switch(action) {
                    case ADD -> hits.add(new RayHit<>(block, this.position.cpy(), distance));
                    case TELEPORT -> hits.add(new RayHit<>(block, rayPosition, distance));
                    case STOP -> {
                        hits.add(new RayHit<>(block, this.position.cpy(), distance));
                        break castCycle;
                    }
                }
            }
        }

        return hits;
    }

}
