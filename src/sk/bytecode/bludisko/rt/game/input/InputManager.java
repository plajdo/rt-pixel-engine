package sk.bytecode.bludisko.rt.game.input;

import javax.swing.event.MouseInputListener;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyListener;
import java.lang.ref.WeakReference;

/**
 * Input manager that is subscribed to events from Window and passes them further
 * for processing in game.
 * Default implementation handles mouse locking and should be always called
 * via super calls when implemented.
 * @see sk.bytecode.bludisko.rt.game.window.Window
 * @see GameInputManagerDelegate
 */
public abstract class InputManager implements KeyListener, MouseInputListener {

    protected WeakReference<GameInputManagerDelegate> delegate;

    protected Robot robot;
    protected Rectangle windowDimensions;

    protected boolean mouseLocked = false;

    // MARK: - Constructor

    /**
     * Creates the Input Manager and a robot object for locking the mouse in the Window.
     * Might cause a warning/permissions request to show up on macOS systems.
     */
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

    /**
     * Tick the input manager. Should be called before drawing a frame to
     * update input information accordingly.
     * @param dt Time passed since the last frame has finished processing.
     */
    public void tick(float dt) {
        if(mouseLocked) {
            centerMouse();
        }
    }

    /**
     * Sets the delegate whose methods are called, when a change in input
     * is detected.
     * @param delegate Object implementing the delegate methods.
     * @see GameInputManagerDelegate
     */
    public void setDelegate(GameInputManagerDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    /**
     * Notify the Input Manager of new Window dimensions.
     * @param newDimensions New Window bounds.
     */
    public void updateWindowDimensions(Rectangle newDimensions) {
        this.windowDimensions = newDimensions;
    }

    /**
     * Sets the mouse lock on this window.
     * @param locked Whether the mouse should be locked in the centre.
     */
    public void setMouseLocked(boolean locked) {
        this.mouseLocked = locked;
    }

}
