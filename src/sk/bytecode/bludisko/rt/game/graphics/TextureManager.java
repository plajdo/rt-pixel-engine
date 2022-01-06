package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.math.MathUtils;

public class TextureManager {

    private static Texture[] generatedTextures;
    private static Texture[] loadedTextures;

    private static Texture noTexture;

    // MARK: - Public

    public static void loadAll() {
        generateTextures();
        loadTextures();

        noTexture = new Texture();
    }

    public static Texture getTexture(int texture) {
        return loadedTextures[texture];
    }

    public static Texture getGenerated(int texture) {
        return generatedTextures[texture];
    }

    public static Texture getEmpty() {
        return noTexture;
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
        //loadedTextures[6] = new Texture("bluePortalTall");

        loadedTextures[7] = new Texture("glass/glass");
        loadedTextures[8] = new Texture("glass/glass_2");

        loadedTextures[9] = new Texture("floor/darkFloorTile");
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

}
