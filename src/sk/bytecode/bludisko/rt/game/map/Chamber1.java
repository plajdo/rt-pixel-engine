package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.blocks.game.Portal;
import sk.bytecode.bludisko.rt.game.entities.Player;

import javax.swing.JOptionPane;

public class Chamber1 extends World {

    public Chamber1() {
        setupMap();
    }

    @Override
    public void tick(float dt) {
        Player player = playerRef.get();
        if(player != null) {
            if(playerAtDoors(player)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Vdaka za vyskusanie!\nFilip Sasala, 2022",
                        "Raycasting \"Portal\" demo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                System.exit(0);
            }
        }
    }

    private void setupMap() {
        this.map = GameMap.load("chamber1");
        var p1 = ((Portal) map.walls().getBlock(7, 7));
        var p2 = ((Portal) map.walls().getBlock(4, 9));

        p1.setOtherPortal(p2);
        p2.setOtherPortal(p1);
    }

    private boolean playerAtDoors(Player player) {
        return player.getPosition().dst2(8f, 4.5f) < 1;
    }

}
