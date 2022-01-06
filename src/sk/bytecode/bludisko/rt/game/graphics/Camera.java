package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.entities.Entity;
import sk.bytecode.bludisko.rt.game.map.GameMap;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.util.Config;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Camera {

    private GameMap map;

    private final Vector2 position;
    private final Vector2 direction;
    private final Vector2 plane;

    private float positionZ;
    private float pitch;

    private final Vector2 viewportSize;
    private Rectangle screenSize;

    // MARK: - Constructor

    public Camera() {
        this.position = new Vector2(0f, 0f);
        this.positionZ = 0f;

        this.direction = new Vector2(0f, -1f);
        this.pitch = 0;

        this.plane = direction.cpy().rotate90(-1).scl(2f / 3f);
        this.viewportSize = new Vector2(320, 240);
        this.screenSize = new Rectangle(320, 240);
    }

    // MARK: - Game loop

    public synchronized void draw(Graphics graphics) {
        BufferedImage bufferedImage = new BufferedImage(
                (int) viewportSize.x,
                (int) viewportSize.y,
                BufferedImage.TYPE_INT_ARGB
        );
        final int[] screenBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        drawCeiling(screenBuffer);
        drawFloor(screenBuffer);
        drawWalls(screenBuffer);

        int width = (int) (viewportSize.x / Config.Display.DRAWING_QUALITY);
        int height = (int) (viewportSize.y / Config.Display.DRAWING_QUALITY);
        int imgTopLeftX = (screenSize.width / 2) - (width / 2);
        int imgTopLeftY = (screenSize.height / 2) - (height / 2);

        graphics.setColor(java.awt.Color.green);
        graphics.drawImage(bufferedImage, imgTopLeftX, imgTopLeftY, width, height, null);
        graphics.drawString(this.position.toString(), 0, 50);
    }

    private void drawWalls(final int[] screenBuffer) {
        for(int i = 0; i < (int) viewportSize.x; i++) {
            float screenX = 2f * i / (int) viewportSize.x - 1f;
            Vector2 rayDirection = new Vector2(
                    this.direction.x + this.plane.x * screenX,
                    this.direction.y + this.plane.y * screenX
            ).nor();

            var ray = new BlockRay(map.walls(), this.position.cpy(), rayDirection);
            var zBuffer = ray.cast(Config.Display.RENDER_DISTANCE);

            for(int j = zBuffer.size() - 1; j >= 0; j--) {
                RayHit<Block> hit = zBuffer.get(j);

                // Fish eye fix
                float distance = (float) (hit.distance() * Math.abs(Math.sin(plane.angleRad() - rayDirection.angleRad())));

                Side hitSide = hit.result().getHitSide(hit.position());

                Vector2 hitPosition = hit.position();
                Vector2 wallHitCoordinates = hitPosition.cpy()
                        .sub((float) Math.floor(hitPosition.x), (float) Math.floor(hitPosition.y));

                Texture texture = hit.result().getTexture(hitSide);
                int textureWidth = texture.getWidth();
                int textureHeight = texture.getHeight();

                int texelX_X = textureWidth - (int)(wallHitCoordinates.x * textureWidth) - 1;
                int texelX_Y = textureWidth - (int)(wallHitCoordinates.y * textureWidth) - 1;
                int texelX = Math.min(texelX_X, texelX_Y);

                int screenWidth = (int) viewportSize.x;
                int screenHeight = (int) viewportSize.y;
                int marginalObjectHeight = (int) (screenHeight / distance);

                int horizon = screenHeight / 2;
                int eyeLevel = marginalObjectHeight / 2;
                int floorLevel = eyeLevel + horizon;

                float objectHeight = hit.result().getHeight() * marginalObjectHeight;
                int objectTop = (int) ((floorLevel - objectHeight / 2) + pitch + (positionZ / distance));
                int objectBottom = (int) (floorLevel + pitch + (positionZ / distance));

                int loopStart = MathUtils.clamp(objectTop, 0, screenHeight);
                int loopEnd = MathUtils.clamp(objectBottom, 0, screenHeight);

                float texelStep = 1f * textureHeight / (marginalObjectHeight * hit.result().getHeight() / 2);
                float texelPosition = texelStep * Math.max(-objectTop, 1);

                float colorScale = 1 - ((hitSide == Side.EAST || hitSide == Side.WEST ? 0 : 1) * 0.33f);

                for(int k = loopStart; k < loopEnd; k++) {
                    int texelY = (int)texelPosition & (textureHeight - 1);
                    texelPosition += texelStep;

                    Color color = texture.getColor(texelX, texelY);

                    if(k >= 0 && k < screenHeight) {
                        int currentColor = screenBuffer[i + k * screenWidth];
                        screenBuffer[i + k * screenWidth] = color.fade(currentColor).scaled(colorScale).argb();
                    }
                }
            }
        }
    }

    private void drawFloor(final int[] screenBuffer) {
        Vector2 rayDirectionLeft = new Vector2(direction).sub(plane);
        Vector2 rayDirectionRight = new Vector2(direction).add(plane);

        int screenWidth = (int) viewportSize.x;
        int screenHeight = (int) viewportSize.y;

        int horizon = screenHeight / 2;

        for(int y = horizon + (int) pitch; y < (int) viewportSize.y; y++) {
            if(y < 0) continue;

            float cameraY = y - (screenHeight / 2f) - pitch;
            float cameraZ = (screenHeight / 2f) + positionZ;

            float floorDistance = cameraZ / cameraY;

            Vector2 marginalFloorDistance = new Vector2(floorDistance, floorDistance)
                    .scl(rayDirectionRight.cpy().sub(rayDirectionLeft))
                    .scl(1f / screenWidth);

            Vector2 floorCoordinates = new Vector2(position)
                    .add(rayDirectionLeft.cpy().scl(floorDistance));

            for(int x = 0; x < screenWidth; x++) {
                Texture texture = map.floor().getBlocksAt(floorCoordinates)[0].getTexture(Side.NONE);
                Vector2 floorTilePosition = MathUtils.decimalPart(floorCoordinates);
                int textureX = (int) Math.min(texture.getWidth() * floorTilePosition.x, texture.getWidth() - 1);
                int textureY = (int) Math.min(texture.getHeight() * floorTilePosition.y, texture.getHeight() - 1);

                floorCoordinates.add(marginalFloorDistance);

                int color = texture.getColor(textureX, textureY).argb();

                screenBuffer[x + y * screenWidth] = color;
            }
        }
    }

    private void drawCeiling(int[] buffer) {
        Arrays.fill(buffer, map.getCeilingColor());
    }

    // MARK: - Private

    private void updateCameraPlane() {
        this.plane.set(
                direction.cpy()
                        .rotate90(-1)
                        .scl(2f / 3f)
                        .scl((float) screenSize.width / (float) screenSize.height / 1.33333f)
        );
    }

    // MARK: - Modifiers

    public synchronized void move(Vector2 length) {
        position.add(length);
    }

    public synchronized void rotate(float angleDeg, float pitch) {
        direction.rotateDeg(angleDeg);
        plane.rotateDeg(angleDeg);

        this.pitch += pitch;
        if(this.pitch < -200) {
            this.pitch = -200;
        }
        if(this.pitch > 200) {
            this.pitch = 200;
        }
    }

    public void bind(Entity entity) {
        this.map = entity.getWorld().getMap();
        this.position.set(entity.getPosition());
        this.direction.set(entity.getDirection());

        this.positionZ = entity.getPositionZ();
        this.pitch = entity.getPitch();

        updateCameraPlane();
    }

    // MARK: - Setters

    public synchronized void setScreenSize(Rectangle size) {
        this.screenSize = size;

        var width = Math.max(size.width, size.height);
        var height = width / 1.33333f;

        this.viewportSize.set(
                width * Config.Display.DRAWING_QUALITY,
                height * Config.Display.DRAWING_QUALITY
        );
        updateCameraPlane();
    }

}
