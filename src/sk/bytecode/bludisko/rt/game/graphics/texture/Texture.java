package sk.bytecode.bludisko.rt.game.graphics.texture;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.function.ToIntBiFunction;

public class Texture {

    private final BufferedImage image;

    // MARK: - Constructor

    /**
     * Loads a texture from file textureName.png.
     * @param textureName Texture name without file extension.
     * @throws IOException When an error occurs during loading.
     */
    Texture(@NotNull String textureName) throws IOException {
        image = ImageIO.read(new File(textureName + ".png"));
    }

    /**
     * Generate texture based on provided generator.
     * Texture is always 64x64 ARGB.
     * @param texelGenerator Texel generator. Accepts [x, y] as its parameters,
     *                       returns an int representing 32-bit colour.
     */
    Texture(@NotNull ToIntBiFunction<Integer, Integer> texelGenerator) {
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

    public int getRGB(int x, int y) {
        return image.getRGB(x, y);
    }

}
