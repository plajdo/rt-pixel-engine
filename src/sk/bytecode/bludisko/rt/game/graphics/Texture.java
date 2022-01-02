package sk.bytecode.bludisko.rt.game.graphics;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.function.ToIntBiFunction;

public class Texture {

    public static final int SIZE = 64;

    private final BufferedImage image;

    // MARK: - Constructor

    /**
     * Creates an empty texture with dimensions 64 by 64.
     */
    public Texture() {
        this.image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Loads a texture from file textureName.png.
     * @param textureName Texture name without file extension.
     * @throws IOException When an error occurs during loading.
     */
    public Texture(@NotNull String textureName) throws IOException {
        image = ImageIO.read(new File(textureName + ".png"));
    }

    /**
     * Generate texture based on provided generator.
     * Texture is always 64x64 ARGB.
     * @param texelGenerator Texel generator. Accepts [x, y] as its parameters,
     *                       returns an int representing 32-bit colour.
     */
    public Texture(@NotNull ToIntBiFunction<Integer, Integer> texelGenerator) {
        int[] texelArray = new int[SIZE * SIZE];
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                texelArray[(x * SIZE) + y] = texelGenerator.applyAsInt(x, y);
            }
        }

        BufferedImage generatedImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        final int[] imageTexelArray = ((DataBufferInt) generatedImage.getRaster().getDataBuffer()).getData();

        System.arraycopy(texelArray, 0, imageTexelArray, 0, texelArray.length);

        image = generatedImage;
    }

    // MARK: - Public

    public Color getRGB(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

}
