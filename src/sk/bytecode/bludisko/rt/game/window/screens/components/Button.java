package sk.bytecode.bludisko.rt.game.window.screens.components;

import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Button extends View {

    private final Color buttonColor = Color.getHSBColor(0f, 0f, 0.17f);

    private final Label textLabel = new Label();
    private float selectedMultiplier = 1f;

    private Runnable action;

    public Button() {
        addSubview(textLabel);
    }

    @Override
    public void draw(Graphics graphics) {
        int backgroundX = (int) (frame.x + ((frame.width) - (frame.width * selectedMultiplier)) / 2);
        int backgroundY = (int) (frame.y + ((frame.height) - (frame.height * selectedMultiplier)) / 2);

        graphics.setColor(buttonColor);
        graphics.fillRoundRect(
                backgroundX,
                backgroundY,
                (int) (frame.width * selectedMultiplier),
                (int) (frame.height * selectedMultiplier),
                12,
                12
        );

        super.draw(graphics);
    }

    @Override
    public void setFrame(Rectangle frame) {
        super.setFrame(frame);
        textLabel.setFrame(getFrame());
    }

    // MARK: - Touch handling

    @Override
    public void touchesBegan(Point point) {
        super.touchesBegan(point);
        selectedMultiplier = 1.05f;
    }

    @Override
    public void touchesEnded(Point point) {
        super.touchesEnded(point);
        selectedMultiplier = 1f;

        NullSafe.accept(action, Runnable::run);
    }

    @Override
    public void touchesCancelled(Point point) {
        super.touchesCancelled(point);
        selectedMultiplier = 1f;
    }

    // MARK: - Getters and Setters

    public String getButtonText() {
        return textLabel.getText();
    }

    public void setButtonText(String buttonText) {
        textLabel.setText(buttonText);
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
