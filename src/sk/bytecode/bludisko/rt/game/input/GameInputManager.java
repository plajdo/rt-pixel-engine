package sk.bytecode.bludisko.rt.game.input;

import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameInputManager extends InputManager {

    private int direction = 0b0000;

    // MARK: - Private

    private void toggleDirectionOn(int keyCode) {
        this.direction = switch(keyCode) {
            case Keybinds.UP -> direction | (1 << 3);
            case Keybinds.LEFT -> direction | (1 << 2);
            case Keybinds.DOWN -> direction | (1 << 1);
            case Keybinds.RIGHT -> direction | (1);
            default -> direction;
        };
    }

    private void toggleDirectionOff(int keyCode) {
        this.direction = switch(keyCode) {
            case Keybinds.UP -> direction & ~(1 << 3);
            case Keybinds.LEFT -> direction & ~(1 << 2);
            case Keybinds.DOWN -> direction & ~(1 << 1);
            case Keybinds.RIGHT -> direction & ~(1);
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

    // MARK: - KeyListener

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        toggleDirectionOn(e.getKeyCode());

        this.delegate.didUpdateDirection(toVector(direction));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleDirectionOff(e.getKeyCode());

        this.delegate.didUpdateDirection(toVector(direction));
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
        System.out.println(-(e.getX() - 320));
        this.delegate.didUpdateRotation(new Vector2(-(e.getX() - 320), e.getY()));
    }

}
