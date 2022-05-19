package sk.bytecode.bludisko.rt.game.window.screens.components;

import java.awt.Color;
import java.awt.Graphics;

public class ColorView extends View {

    private Color backgroundColor;

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        graphics.setColor(backgroundColor);
        graphics.fillRect(frame.x, frame.y, frame.width, frame.height);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
