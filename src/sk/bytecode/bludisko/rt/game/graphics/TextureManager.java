package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.math.MathUtils;

/**
 * Contains useful methods for texture handling.
 * Fully static class. Cannot be instantiated.
 */
public class TextureManager {

    private static Texture[] generatedTextures;
    private static Texture[] loadedTextures;

    private static Texture noTexture;

    // MARK: - Constructor

    private TextureManager() {}

    // MARK: - Public

    /**
     * Loads and generates all textures it's defined.
     * Must be called before any rendering happens.
     */
    public static void loadAll() {
        generateTextures();
        loadTextures();

        noTexture = new Texture();
    }

    // MARK: - Private

    private static void loadTextures() {
        loadedTextures = new Texture[50];
        loadedTextures[0] = new Texture("wall/wallTile_1");
        loadedTextures[1] = new Texture("wall/darkWallTile_1");
        loadedTextures[2] = new Texture("wall/wallTile_2");
        loadedTextures[3] = new Texture("wall/darkWallTile_2");

        loadedTextures[4] = new Texture("portal/bluePortal_2");
        loadedTextures[5] = new Texture("portal/orangePortal_2");
        loadedTextures[6] = new Texture("wall/board_0");

        loadedTextures[7] = new Texture("glass/glass");
        loadedTextures[8] = new Texture("glass/glass_2");

        loadedTextures[9] = new Texture("floor/darkFloorTile");

        loadedTextures[10] = new Texture("wall/wallWindow");
        loadedTextures[11] = new Texture("wall/wallWindowMirrored");
        loadedTextures[12] = new Texture("wall/doors");

        loadedTextures[13] = new Texture("items/portalGun");
    }

    private static void generateTextures() {
        generatedTextures = new Texture[8];

        generatedTextures[0] = new Texture((x, y) -> MathUtils.INT_MSB_MASK | 65536 * 192 * ((x % 16) & (y % 16)));
        generatedTextures[1] = new Texture((x, y) ->
                MathUtils.INT_MSB_MASK | (y * 128 / 64 + x * 128 / 64) +
                (1 << 8) * (y * 128 / 64 + x * 128 / 64) +
                (1 << 16) * (y * 128 / 64 + x * 128 / 64));
        generatedTextures[2] = new Texture((x, y) ->
                MathUtils.INT_MSB_MASK | 256 * (y * 128 / 64 + x * 128 / 64) + 65536 * (y * 128 / 64 + x * 128 / 64));
        generatedTextures[3] = new Texture((x, y) -> MathUtils.INT_MSB_MASK | 256 * ((x * 256 / 64) ^ (y * 256 / 64)));
        generatedTextures[4] = new Texture((x, y) -> MathUtils.INT_MSB_MASK | ((x ^ y) * 4) + ((x ^ y) * 262144));
        generatedTextures[5] = new Texture((x, y) -> 0x3FBCD2F5);
        generatedTextures[6] = new Texture((x, y) -> 0xFF5F3F00);
        generatedTextures[7] = new Texture((x, y) -> 0xFF3F5F00);
    }

    // MARK: - Getters

    /**
     * Returns a texture with an ID.
     * @param texture ID - index in an array containing all textures loaded from files
     * @return Texture
     */
    public static Texture getTexture(int texture) {
        return loadedTextures[texture];
    }

    /**
     * Returns a generated texture with an ID.
     * @param texture ID - index in an array containing all generated textures
     * @return Texture
     */
    public static Texture getGenerated(int texture) {
        return generatedTextures[texture];
    }

    /**
     * Returns an empty, transparent 64x64px texture.
     * @return Texture
     */
    public static Texture getEmpty() {
        return noTexture;
    }

}
