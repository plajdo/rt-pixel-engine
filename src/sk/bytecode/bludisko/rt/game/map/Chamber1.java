package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.entities.Player;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import javax.swing.JOptionPane;

/**
 * First map that loads after opening the game.
 * Based on zero-th testing chamber from the game Portal.
 */
public class Chamber1 extends World {

    // MARK: - Constructor

    public Chamber1() {
        setupMap();
    }

    // MARK: - Game loop

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

    // MARK: - Private

    private void setupMap() {
        this.map = GameMap.load("chamber1");
        PortalManager.createPortal(map, new Vector2(7.5f, 8.0f), new Vector2(5.0f, 9.5f));
    }

    private boolean playerAtDoors(Player player) {
        return player.getPosition().dst2(8f, 4.5f) < 1;
    }

}
