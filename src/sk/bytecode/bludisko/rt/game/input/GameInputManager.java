package sk.bytecode.bludisko.rt.game.input;

import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.util.Config;

import java.awt.Point;
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
        if(keyCode == Config.Keybinds.UP) {
            this.direction = this.direction | (1 << 3);
        } else if(keyCode == Config.Keybinds.LEFT) {
            this.direction = this.direction | (1 << 2);
        } else if(keyCode == Config.Keybinds.DOWN) {
            this.direction = this.direction | (1 << 1);
        } else if(keyCode == Config.Keybinds.RIGHT) {
            this.direction = this.direction | (1);
        }
    }

    private void toggleDirectionOff(int keyCode) {
        if(keyCode == Config.Keybinds.UP) {
            this.direction = this.direction & ~(1 << 3);
        } else if(keyCode == Config.Keybinds.LEFT) {
            this.direction = this.direction & ~(1 << 2);
        } else if(keyCode == Config.Keybinds.DOWN) {
            this.direction = this.direction & ~(1 << 1);
        } else if(keyCode == Config.Keybinds.RIGHT) {
            this.direction = this.direction & ~(1);
        } else {
            this.direction = direction;
        }
    }

    private Vector2 toVector(int direction) {
        Vector2 directionVector = new Vector2();
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

    private void withDelegate(Consumer<GameInputManagerDelegate> action) {
        GameInputManagerDelegate delegate;
        if((this.delegate != null) && (delegate = this.delegate.get()) != null) {
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
            robot.mouseMove(0, 0);
            this.mouseLocked = !this.mouseLocked;
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
        Point newPosition = e.getLocationOnScreen();
        newPosition.translate(-this.windowDimensions.x, this.windowDimensions.y);
        newPosition.translate(-this.windowDimensions.width / 2, -this.windowDimensions.height / 2);

        withDelegate(d -> d.didUpdateRotation(new Vector2(-newPosition.x, -newPosition.y)));
    }

}
