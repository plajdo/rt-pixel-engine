package sk.bytecode.bludisko.rt.game.window.screens.components;

import java.awt.Color;
import java.awt.Graphics;

public class Label extends View {

    private String text = "";
    private final Color textColor = Color.WHITE;

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        var stringBounds = graphics
                .getFontMetrics()
                .getStringBounds(text, graphics);

        var stringX = frame.x + (frame.width / 2) - (int) (stringBounds.getWidth() / 2);
        var stringY = frame.y + (frame.height / 2) + (int) (stringBounds.getHeight() / 2);

        graphics.setColor(textColor);
        graphics.drawString(text, stringX, stringY);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
