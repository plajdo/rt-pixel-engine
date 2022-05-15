package sk.bytecode.bludisko.rt.game.graphics;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.entities.Player;
import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;

/**
 * Class responsible for drawing overlays to the screen. Overlays are
 * always drawn after the main frame has finished rendering.
 * Includes drawing currently held item by the player and a crosshair.
 * @see Player#getHeldItem()
 * @see sk.bytecode.bludisko.rt.game.window.screens.GameScreen
 */
public final class Overlay implements Actor {

    // MARK: - Attributes

    private WeakReference<Player> player;
    private Rectangle screenSize;

    // MARK: - Override

    @Override
    public void tick(float dt) {

    }

    @Override
    public void draw(@NotNull Graphics graphics) {
        drawItemOverlay(graphics);
    }

    @Override
    public void screenDidChangeBounds(@NotNull Rectangle canvas) {
        screenSize = canvas;
    }

    // MARK: - Private

    private void drawItemOverlay(@NotNull Graphics graphics) {
        NullSafe.acceptWeak(player, player -> NullSafe.accept(player.getHeldItem(), heldItem -> {
            var texture = heldItem.getOverlay();
            var image = texture.asImage();

            float resizingRatio = screenSize.height / (float)texture.getHeight();
            int scaledWidth = (int) (texture.getWidth() * resizingRatio);
            int offset = (screenSize.width - scaledWidth) / 2;

            graphics.drawImage(image, offset, 0, scaledWidth, screenSize.height, null);
        }));
    }

    // MARK: - Public

    public void connectPlayer(Player player) {
        this.player = new WeakReference<>(player);
    }

}
