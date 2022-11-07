package sk.bytecode.bludisko.rt.game.graphics;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.function.ToIntBiFunction;

/**
 * Class representing and holding a texture. Mainly used by TextureManager
 * to handle textures.
 * @see TextureManager
 */
public class Texture {

    private final BufferedImage image;

    // MARK: - Constructor

    /**
     * Creates an empty texture with dimensions 64 by 64.
     */
    public Texture() {
        this.image = emptyImage();
    }

    /**
     * Loads a texture from file textureName.png.
     * @param textureName Texture name without file extension.
     */
    public Texture(@NotNull String textureName) {
        this.image = loadImage(textureName);
    }

    /**
     * Generate texture based on provided generator.
     * Texture is always 64x64 ARGB.
     * @param texelGenerator Texel generator. Accepts (x, y) as its parameters,
     *                       returns an int representing 32-bit ARGB colour for pixel with coordinates (x, y).
     */
    public Texture(@NotNull ToIntBiFunction<Integer, Integer> texelGenerator) {
        int[] texelArray = new int[64 * 64];
        for(int x = 0; x < 64; x++) {
            for(int y = 0; y < 64; y++) {
                texelArray[(x * 64) + y] = texelGenerator.applyAsInt(x, y);
            }
        }

        BufferedImage generatedImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        final int[] imageTexelArray = ((DataBufferInt) generatedImage.getRaster().getDataBuffer()).getData();

        System.arraycopy(texelArray, 0, imageTexelArray, 0, texelArray.length);

        image = generatedImage;
    }

    // MARK: - Private

    private BufferedImage emptyImage() {
        return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
    }

    private BufferedImage loadImage(String textureName) {
        try {
            return ImageIO.read(new File("res/textures/" + textureName + ".png"));
        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("Could not load texture: " + e.getLocalizedMessage());
            return emptyImage();
        }
    }

    // MARK: - Getters

    /**
     * @param x x coordinate
     * @param y y coordinate
     * @return Color of a pixel at coordinates (x, y)
     * @see Color
     */
    public Color getColor(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    /**
     * @return Texture width in pixels
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * @return Texture height in pixels
     */
    public int getHeight() {
        return image.getHeight();
    }

    /**
     * Returns BufferedImage from this texture. Texture is stored as
     * a BufferedImage internally, thus method has no performance
     * overhead.
     * @return BufferedImage from this texture.
     */
    public BufferedImage asImage() {
        return image;
    }

}
