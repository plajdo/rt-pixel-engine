package sk.bytecode.bludisko.rt.game.window;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.util.Config;
import sk.bytecode.bludisko.rt.game.window.screens.Screen;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Main application window containing a {@link Canvas} to be drawn on.
 * Displays a {@link Screen} that splits the rendering pipeline further.
 * This class invokes every tick and draw call in the game loop
 * and handles global rendering quality settings. It also listens
 * for all system I-O events on the main window {@link JFrame} and
 * handles window movement/resize events.
 *
 * @see Screen
 * @see Canvas
 * @see Graphics
 * @see JFrame
 * @see InputManager
 */
public final class Window {

    private static final int FRAMERATE = 60;
    private static final float FRAME_TIME = 1000f / FRAMERATE;

    private final Dimension windowSize;
    private final Canvas canvas;
    private final JFrame frame;
    private Screen screen;

    private InputManager inputManager;

    private boolean running = false;
    private boolean cursorVisible = true;

    // MARK: - Initialize

    /**
     * Creates main game window with {@link Canvas}.
     * Default resolution is 640x480px, however that can be
     * resized later.
     * @param screen Game screen to show inside the Canvas
     */
    public Window(@NotNull Screen screen) {
        this.windowSize = new Dimension(640, 480);
        this.frame = new JFrame("rt_portal_demo");
        this.canvas = new Canvas();

        this.setupCanvas();
        this.setupFrame();
        this.setupScreen(screen);
        this.frame.requestFocus();
    }

    // MARK: - Private

    private void setupCanvas() {
        this.canvas.setPreferredSize(this.windowSize);
    }

    private void setupFrame() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.addComponentListener(this.getResizeListener());

        this.frame.add(this.canvas);

        this.frame.pack(); // sizeToFit
        this.frame.setVisible(true);
    }

    private void setupScreen(Screen screen) {
        this.canvas.removeKeyListener(this.inputManager);
        this.canvas.removeMouseListener(this.inputManager);
        this.canvas.removeMouseMotionListener(this.inputManager);

        screen.screenWillAppear(this);

        this.inputManager = screen.getInputManager();
        this.screen = screen;

        this.canvas.addKeyListener(this.inputManager);
        this.canvas.addMouseListener(this.inputManager);
        this.canvas.addMouseMotionListener(this.inputManager);

        screen.screenDidAppear();
    }

    private void setupCursor() {
        if (this.cursorVisible) {
            this.frame.setCursor(Cursor.getDefaultCursor());
            return;
        }

        this.frame.setCursor(
                this.frame.getToolkit().createCustomCursor(
                        new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                        new Point(0, 0),
                        "Blank cursor"
                )
        );
    }

    private ComponentAdapter getResizeListener() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                var component = e.getComponent();
                if (Window.this.screen != null && component != null) {
                    Window.this.updateBounds(component);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                var component = e.getComponent();
                if (Window.this.screen != null && component != null) {
                    Window.this.updateBounds(component);
                }
            }
        };
    }

    private void updateBounds(@NotNull Component component) {
        Rectangle bounds = component.getBounds();
        this.screen.screenDidChangeBounds(bounds);
        this.windowSize.setSize(bounds.width, bounds.height);
    }

    // MARK: - Public

    /**
     * Replaces the currently shown screen inside the Window
     * with a different one.
     * @param screen Replacement screen
     */
    public void setScreen(@NotNull Screen screen) {
        this.setupScreen(screen);
    }

    /**
     * Sets cursor visibility over this Window.
     * @param visible Whether cursor should be visible
     */
    public void setCursorVisible(boolean visible) {
        this.cursorVisible = visible;
        this.setupCursor();
    }

    /**
     * Get current screen bounds, including window size and position.
     * @return Bounds rectangle
     */
    public Rectangle canvasBounds() {
        return this.canvas.getBounds();
    }

    // MARK: - Game thread

    /**
     * Starts the game loop on a new Thread.
     * Does not block, returns immediately.
     */
    public void start() {
        this.running = true;
        Runnable gameThread = () -> {
            try {
                this.startGameCycle();
            } catch (InterruptedException e) {
                System.err.println(e.getLocalizedMessage());
            }
        };
        gameThread.run();
    }

    private void startGameCycle() throws InterruptedException {
        long lastFrameTime = 0;
        while (this.running) {
            long currentTime = System.currentTimeMillis();

            float deltaMilliseconds = currentTime - lastFrameTime;
            this.updateDrawingQuality(deltaMilliseconds);

            if (deltaMilliseconds > FRAME_TIME) {
                lastFrameTime = System.currentTimeMillis();

                float deltaSeconds = deltaMilliseconds * 0.001f;
                this.tick(deltaSeconds);
                this.drawFrame();
            } else {
                synchronized (this) {
                    this.wait((long)FRAME_TIME / 2);
                }
            }
        }
    }

    private void tick(float delta) {
        this.screen.tick(delta);
    }

    private void updateDrawingQuality(float dt) {
        if (dt > FRAME_TIME * 2) {
            Config.Display.setDrawingQuality(Config.Display.getDrawingQuality() * 0.9f);
            this.screen.screenDidChangeBounds(
                    new Rectangle(
                            this.windowSize.width,
                            this.windowSize.height
                    )
            );
        }
        if (dt < FRAME_TIME / 2) {
            if (Config.Display.getDrawingQuality() < 1) {
                Config.Display.setDrawingQuality(Config.Display.getDrawingQuality() * 1.1f);
                this.screen.screenDidChangeBounds(
                        new Rectangle(
                                this.windowSize.width,
                                this.windowSize.height
                        )
                );
            }
        }
    }

    // MARK: - Graphics

    private void drawFrame() {
        BufferStrategy bufferStrategy = this.canvas.getBufferStrategy();
        if (bufferStrategy == null) {
            this.canvas.createBufferStrategy(2);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        this.clearScreen(graphics);

        this.screen.draw(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    private void clearScreen(@NotNull Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, this.windowSize.width, this.windowSize.height);
    }

}
