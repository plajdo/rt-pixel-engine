package sk.bytecode.bludisko.rt.game.window.screens.components;

import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.awt.Graphics;

public class Image extends View {

    private Texture texture;

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        NullSafe.accept(texture, texture -> graphics.drawImage(
                texture.asImage(),
                frame.x,
                frame.y,
                frame.width,
                frame.height,
                null
        ));
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

}
