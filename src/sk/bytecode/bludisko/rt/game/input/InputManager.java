package sk.bytecode.bludisko.rt.game.input;

import javax.swing.event.MouseInputListener;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyListener;
import java.lang.ref.WeakReference;

public abstract class InputManager implements KeyListener, MouseInputListener {

    protected WeakReference<GameInputManagerDelegate> delegate; // TODO: weak reference!

    protected Robot robot;
    protected Rectangle windowDimensions;

    protected boolean mouseLocked = false;

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
            int centerX = windowDimensions.x + windowDimensions.width / 2;
            int centerY = windowDimensions.y + windowDimensions.height / 2;
            robot.mouseMove(centerX, centerY);
        }
    }

    // MARK: - Public

    public void tick(float dt) {
        if(mouseLocked) {
            centerMouse();
        }
    }

    public void setDelegate(GameInputManagerDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    public void updateWindowDimensions(Rectangle newDimensions) {
        this.windowDimensions = newDimensions;
    }

    public void setMouseLocked(boolean locked) {
        this.mouseLocked = locked;
    }

}
