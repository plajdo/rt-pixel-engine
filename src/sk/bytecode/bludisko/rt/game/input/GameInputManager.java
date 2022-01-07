package sk.bytecode.bludisko.rt.game.input;

import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.util.Config;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * Custom implementation of Input Manager for
 * a Game Screen.
 */
public class GameInputManager extends InputManager {

    private int direction = 0b0000;

    // MARK: - Private

    private void toggleDirectionOn(int keyCode) {
        this.direction = switch(keyCode) {
            case Config.Keybinds.UP -> direction | (1 << 3);
            case Config.Keybinds.LEFT -> direction | (1 << 2);
            case Config.Keybinds.DOWN -> direction | (1 << 1);
            case Config.Keybinds.RIGHT -> direction | (1);
            default -> direction;
        };
    }

    private void toggleDirectionOff(int keyCode) {
        this.direction = switch(keyCode) {
            case Config.Keybinds.UP -> direction & ~(1 << 3);
            case Config.Keybinds.LEFT -> direction & ~(1 << 2);
            case Config.Keybinds.DOWN -> direction & ~(1 << 1);
            case Config.Keybinds.RIGHT -> direction & ~(1);
            default -> direction;
        };
    }

    private Vector2 toVector(int direction) {
        var directionVector = new Vector2();
        directionVector.add(
                (direction & 0b1000) > 0 ? 1 : 0,
                (direction & 0b0100) > 0 ? 1 : 0
        );
        directionVector.sub(
                (direction & 0b0010) > 0 ? 1 : 0,
                (direction & 0b0001) > 0 ? 1 : 0
        );
        directionVector.nor();
        return directionVector;
    }

    private void toggleSprint(boolean toggle) {
        withDelegate(d -> d.didUpdateSprintingStatus(toggle));
    }

    private void withDelegate(Consumer<IGameInputManagerDelegate> action) {
        IGameInputManagerDelegate delegate;
        if((this.getDelegate() != null) && (delegate = this.getDelegate().get()) != null) {
            action.accept(delegate);
        }
    }

    // MARK: - KeyListener

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        toggleDirectionOn(e.getKeyCode());

        if(e.getKeyCode() == Config.Keybinds.LOCK_MOUSE) {
            this.getRobot().mouseMove(0, 0);
            this.setMouseLocked(!this.isMouseLocked());
        }
        if(e.getKeyCode() == Config.Keybinds.SPRINT) {
            toggleSprint(true);
        }

        withDelegate(d -> d.didUpdateMovementDirection(toVector(this.direction)));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleDirectionOff(e.getKeyCode());

        if(e.getKeyCode() == Config.Keybinds.SPRINT) {
            toggleSprint(false);
        }

        withDelegate(d -> d.didUpdateMovementDirection(toVector(this.direction)));
    }

    // MARK: - MouseListener

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // MARK: - MouseMotionListener

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        var newPosition = e.getLocationOnScreen();
        newPosition.translate(-this.getWindowDimensions().x, this.getWindowDimensions().y);
        newPosition.translate(-this.getWindowDimensions().width / 2, -this.getWindowDimensions().height / 2);

        withDelegate(d -> d.didUpdateRotation(new Vector2(-newPosition.x, -newPosition.y)));
    }

}
