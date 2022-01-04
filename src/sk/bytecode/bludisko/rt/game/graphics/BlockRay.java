package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.util.ArrayList;

public class BlockRay extends Ray {

    private final Map map;

    public BlockRay(Vector2 position, Vector2 direction, Map map) {
        super(position, direction);
        this.map = map;
    }

    public ArrayList<Ray.Hit<Block>> cast(int maxSteps) {
        ArrayList<Ray.Hit<Block>> hits = new ArrayList<>();
        castCycle: for(int i = 0; i < maxSteps; i++) {
            step();

            Block[] blocks = map.getBlocksAt(position);
            for(Block block : blocks) {
                Ray.Result result = block.hitDistance(this);
                switch(result.action()) {
                    case ADD -> {
                        position.add(direction.cpy().scl(result.distance()));
                        hits.add(new Ray.Hit<>(block, this.position.cpy(), distance));
                    }
                    case STOP -> {
                        position.add(direction.cpy().scl(result.distance()));
                        hits.add(new Ray.Hit<>(block, this.position.cpy(), distance));
                        break castCycle;
                    }
                }
            }
        }

        return hits;
    }

}
