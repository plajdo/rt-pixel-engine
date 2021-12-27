package sk.bytecode.bludisko.rt.game.input;

import javax.swing.event.MouseInputListener;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyListener;

public abstract class InputManager implements KeyListener, MouseInputListener {

    protected GameInputManagerDelegate delegate; // TODO: weak reference!

    private Robot robot;
    private Rectangle windowDimensions;

    private boolean mouseLock = false;

    // MARK: - Constructor

    public InputManager() {
        createRobot();
    }

    // MARK: - Private

    private void createRobot() {
        try {
            this.robot = new Robot();
        } catch(AWTException e) {
            throw new RuntimeException("Current environment does not support mouse input!");
        }
    }

    private void centerMouse() {
        if(robot != null && windowDimensions != null) {
            robot.mouseMove(windowDimensions.width / 2, windowDimensions.height / 2);
        }
    }

    // MARK: - Public

    public void tick(float dt) {
        if(mouseLock) {
            centerMouse();
        }
    }

    public void setDelegate(GameInputManagerDelegate delegate) {
        this.delegate = delegate;
    }

    public void updateWindowDimensions(Rectangle newDimensions) {
        this.windowDimensions = newDimensions;
    }

    public void setMouseLock(boolean locked) {
        this.mouseLock = locked;
    }

}
