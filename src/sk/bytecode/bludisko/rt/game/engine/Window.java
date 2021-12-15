package sk.bytecode.bludisko.rt.game.engine;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.screens.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public final class Window {

    private final static Dimension WINDOW_SIZE = new Dimension(640, 480);
    private final static int FRAMERATE = 24;
    private final static float FRAME_TIME = 1000f / FRAMERATE;

    private final Canvas canvas;
    private final JFrame frame;
    private Screen screen;

    private boolean running;

    // MARK: - Initialize

    public Window(Screen screen) {
        this.frame = new JFrame("bludisko_rt");
        this.canvas = new Canvas();

        this.setupCanvas();
        this.setupFrame();
        this.setupScreen(screen);
    }

    private void setupCanvas() {
        this.canvas.setPreferredSize(WINDOW_SIZE);
    }

    private void setupFrame() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.canvas);

        this.frame.pack(); // sizeToFit
        this.frame.setVisible(true);
    }

    private void setupScreen(Screen screen) {
        screen.screenWillAppear(this);
        this.screen = screen;
        screen.screenDidAppear();
    }

    // MARK: - Public

    public void setScreen(Screen screen) {
        setupScreen(screen);
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
        graphics.fillRect(0, 0, WINDOW_SIZE.width, WINDOW_SIZE.height);
    }

}
