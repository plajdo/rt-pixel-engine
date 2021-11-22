package sk.bytecode.bludisko.rt;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

class Screen {

    private final static Dimension WINDOW_SIZE = new Dimension(640, 480);
    private final static float FRAME_TIME = 1000f / 24f;

    private Canvas canvas;
    private JFrame frame;

    private Runnable graphicsThread;
    private boolean running;

    // MARK: - Initialize

    public Screen() {
        this.frame = new JFrame("bludisko_rt");
        this.canvas = new Canvas();

        this.setupCanvas();
        this.setupFrame();


    }

    private void setupCanvas() {
        this.canvas.setPreferredSize(WINDOW_SIZE);

    }

    private void setupFrame() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.canvas);

        this.frame.pack(); // sizeToFit()
        this.frame.setVisible(true);

    }

    // MARK: - Render thread

    public void start() {
        this.running = true;
        this.graphicsThread = () -> {
            try {
                startRenderCycle();
            } catch (InterruptedException e) {
                System.err.println(e.getLocalizedMessage());
            }

        };
        this.graphicsThread.run();

    }

    private void startRenderCycle() throws InterruptedException {

        long lastFrameTime = 0;
        while(running) {
            long currentTime = System.currentTimeMillis();

            if(currentTime - lastFrameTime > FRAME_TIME) {
                lastFrameTime = System.currentTimeMillis();
                drawFrame();

            } else {
                Thread.sleep((long)FRAME_TIME / 2);
            }

        }

    }

    private void drawFrame() {
        render();
    }


    // MARK: - Canvas

    private void render() {
        BufferStrategy bufferStrategy = this.canvas.getBufferStrategy();
        if(bufferStrategy == null) {
            this.canvas.createBufferStrategy(2);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        clearScreen(graphics);

        graphics.dispose();
        bufferStrategy.show();

    }

    private void clearScreen(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WINDOW_SIZE.width, WINDOW_SIZE.height);

    }

}
