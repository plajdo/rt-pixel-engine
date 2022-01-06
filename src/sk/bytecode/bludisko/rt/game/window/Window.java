package sk.bytecode.bludisko.rt.game.window;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.util.Config;
import sk.bytecode.bludisko.rt.game.window.screens.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public final class Window {

    private final static int FRAMERATE = 60;
    private final static float FRAME_TIME = 1000f / FRAMERATE;

    private final Dimension windowSize;
    private final Canvas canvas;
    private final JFrame frame;
    private Screen screen;

    private InputManager inputManager;

    private boolean running = false;
    private boolean cursorVisible = true;

    // MARK: - Initialize

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
        this.canvas.setPreferredSize(windowSize);
    }

    private void setupFrame() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.addComponentListener(getResizeListener());

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
        if(cursorVisible) {
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
                if(screen != null && component != null) {
                    updateBounds(component);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                var component = e.getComponent();
                if(screen != null && component != null) {
                    updateBounds(component);
                }
            }
        };
    }

    private void updateBounds(@NotNull Component component) {
        Rectangle bounds = component.getBounds();
        screen.screenDidChangeBounds(bounds);
        windowSize.setSize(bounds.width, bounds.height);
    }

    // MARK: - Public

    public void setScreen(@NotNull Screen screen) {
        setupScreen(screen);
    }

    public void setCursorVisible(boolean visible) {
        this.cursorVisible = visible;
        this.setupCursor();
    }

    public Rectangle canvasBounds() {
        return canvas.getBounds();
    }

    // MARK: - Game thread

    public void start() {
        this.running = true;
        Runnable gameThread = () -> {
            try {
                startGameCycle();
            } catch (InterruptedException e) {
                System.err.println(e.getLocalizedMessage());
            }
        };
        gameThread.run();
    }

    private void startGameCycle() throws InterruptedException {
        long lastFrameTime = 0;
        while(running) {
            long currentTime = System.currentTimeMillis();

            float deltaMilliseconds = currentTime - lastFrameTime;
            updateDrawingQuality(deltaMilliseconds);

            if(deltaMilliseconds > FRAME_TIME) {
                lastFrameTime = System.currentTimeMillis();

                float deltaSeconds = deltaMilliseconds * 0.001f;
                tick(deltaSeconds);
                drawFrame();
            } else {
                synchronized (this) {
                    wait((long)FRAME_TIME / 2);
                }
            }
        }
    }

    private void tick(float delta) {
        screen.tick(delta);
    }

    private void updateDrawingQuality(float dt) {
        if(dt > FRAME_TIME * 2) {
            Config.Display.DRAWING_QUALITY *= 0.9f;
            screen.screenDidChangeBounds(
                    new Rectangle(
                            this.windowSize.width,
                            this.windowSize.height
                    )
            );
        }
        if(dt < FRAME_TIME / 2) {
            if(Config.Display.DRAWING_QUALITY < 1) {
                Config.Display.DRAWING_QUALITY *= 1.1f;
                screen.screenDidChangeBounds(
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
        if(bufferStrategy == null) {
            this.canvas.createBufferStrategy(2);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        clearScreen(graphics);

        screen.draw(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    private void clearScreen(@NotNull Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, windowSize.width, windowSize.height);
    }

}
