package sk.bytecode.bludisko.rt.game.graphics;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.function.ToIntBiFunction;

public class Texture {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

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
     * @param texelGenerator Texel generator. Accepts [x, y] as its parameters,
     *                       returns an int representing 32-bit colour.
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

    // MARK: - Public

    public Color getColor(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    // MARK: - Private

    private BufferedImage emptyImage() {
        return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
    }

    private BufferedImage loadImage(String textureName) {
        try {
            return ImageIO.read(new File("res/textures/" + textureName + ".png"));
        } catch(IOException e) {
            System.err.println("Could not load texture: " + e.getLocalizedMessage());
            return emptyImage();
        }
    }

}
