package sk.bytecode.bludisko.rt.game.window.screens;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.input.MenuInputManager;
import sk.bytecode.bludisko.rt.game.input.MenuInputManagerDelegate;
import sk.bytecode.bludisko.rt.game.map.Chamber1;
import sk.bytecode.bludisko.rt.game.map.World;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.util.NullSafe;
import sk.bytecode.bludisko.rt.game.window.screens.components.Button;
import sk.bytecode.bludisko.rt.game.window.screens.components.ColorView;
import sk.bytecode.bludisko.rt.game.window.screens.components.Image;
import sk.bytecode.bludisko.rt.game.window.screens.components.Label;
import sk.bytecode.bludisko.rt.game.window.screens.components.View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Main Menu screen.
 */
public class MenuScreen extends Screen implements MenuInputManagerDelegate {

    private final MenuInputManager menuInputManager = new MenuInputManager();
    private final World backgroundWorld;

    private final Camera camera;
    private final View uiView;

    private final ColorView dimBackground;
    private final Image logoImage;
    private final Button playButton;
    private final Button exitButton;
    private final Label bottomLabel;

    @SuppressWarnings("FieldCanBeLocal")
    private final float transitionLength = 2f;
    private float transitionTime = 0f;

    private boolean transition = false;
    private float transitionDirection;
    private float transitionPitch;
    private float transitionHeight;
    private Vector2 transitionVector;

    // MARK: - Initalization

    public MenuScreen() {
        this.backgroundWorld = new Chamber1();

        this.camera = new Camera();
        this.uiView = new View();

        this.dimBackground = new ColorView();
        this.logoImage = new Image();
        this.playButton = new Button();
        this.exitButton = new Button();
        this.bottomLabel = new Label();

        setupBackground();
        setupUI();
        setupInput();
    }

    // MARK: - Private

    private void setupBackground() {
        camera.setMap(backgroundWorld.getMap());
        camera.move(new Vector2(10.5f, 10.5f));
        camera.rotate(250, -100);
        camera.move(500);
    }

    private void setupUI() {
        setupAppearance();
        addSubviews();
    }

    private void setupAppearance() {
        dimBackground.setBackgroundColor(new Color(0.2f, 0.2f, 0.2f, 0.75f));
        logoImage.setTexture(TextureManager.getTexture(15));
        playButton.setButtonText("Start game");
        exitButton.setButtonText("Exit");
        bottomLabel.setText("Filip Šašala, 2022");

        playButton.setAction(this::play);
        exitButton.setAction(this::exit);
    }

    private void addSubviews() {
        uiView.addSubview(dimBackground);
        uiView.addSubview(logoImage);
        uiView.addSubview(playButton);
        uiView.addSubview(exitButton);
        uiView.addSubview(bottomLabel);
    }

    private void setupFrames() {
        var windowFrame = uiView.getFrame();

        dimBackground.setFrame(new Rectangle(15, 35, 390, windowFrame.height - 70));
        logoImage.setFrame(new Rectangle(30, 50, 360, 120));
        playButton.setFrame(new Rectangle(50, 190, 150, 40));
        exitButton.setFrame(new Rectangle(50, 240, 150, 40));
        bottomLabel.setFrame(new Rectangle(30, windowFrame.height - 70 - 15, 150, 30));
    }

    private void setupInput() {
        menuInputManager.setDelegate(this);
        menuInputManager.setMouseLocked(false);
    }

    // MARK: - Button actions

    private void play() {
        NullSafe.acceptWeak(window, window -> window.setCursorVisible(false));
        uiView.setVisible(false);
        transition = true;
    }

    private void exit() {
        System.exit(0);
    }

    // MARK: - Override

    @Override
    public InputManager getInputManager() {
        return menuInputManager;
    }

    @Override
    public void screenDidAppear() {
        super.screenDidAppear();

        NullSafe.acceptWeak(window, window -> window.setCursorVisible(true));
    }

    @Override
    public void screenDidChangeBounds(@NotNull Rectangle bounds) {
        super.screenDidChangeBounds(bounds);

        camera.setScreenSize(bounds);
        uiView.setFrame(bounds);

        setupFrames();
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);

        if(transition) {
            if(transitionTime == 0f) {
                transitionVector = backgroundWorld.getMap()
                        .getSpawnLocation()
                        .cpy()
                        .sub(camera.getPosition());
                transitionDirection = backgroundWorld.getMap()
                        .getSpawnDirection()
                        .angleDeg() - camera.getDirection().angleDeg();
                transitionPitch = 0f - camera.getPitch();
                transitionHeight = 50f - camera.getPositionZ(); // Player default is 50f
            }

            camera.move(transitionVector.cpy()
                    .scl(dt)
                    .scl(1f / transitionLength)
            );
            camera.move(
                    transitionHeight * dt * (1f / transitionLength)
            );
            camera.rotate(
                    transitionDirection * dt * (1f / transitionLength),
                    transitionPitch * dt * (1f / transitionLength)
            );

            transitionTime += dt * (1f / transitionLength);

            if(transitionTime > 1f) {
                NullSafe.acceptWeak(window, window -> window.setScreen(new GameScreen()));
            }
        } else {
            camera.rotate(0.025f, 0);
        }
    }

    @Override
    public void draw(@NotNull Graphics graphics) {
        camera.draw(graphics);
        uiView.draw(graphics);
    }

    // MARK: - MenuInputManagerDelegate

    @Override
    public void touchesBegan(Point point) {
        uiView.touchesBegan(point);
    }

    @Override
    public void touchesEnded(Point point) {
        uiView.touchesEnded(point);
    }

    @Override
    public void touchesCancelled(Point point) {
        uiView.touchesCancelled(point);
    }

}
