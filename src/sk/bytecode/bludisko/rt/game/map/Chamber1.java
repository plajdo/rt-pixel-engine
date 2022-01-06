package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.blocks.walls.Portal;

public class Chamber1 extends World {

    public Chamber1() {
        setupMap();
    }

    @Override
    public void tick(float dt) {

    }

    private void setupMap() {
        this.map = GameMap.load("chamber1");
        var p1 = ((Portal) map.walls().getBlock(7, 7));
        var p2 = ((Portal) map.walls().getBlock(4, 9));

        p1.setOtherPortal(p2);
        p2.setOtherPortal(p1);
    }

}
