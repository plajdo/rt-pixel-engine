package sk.bytecode.bludisko.rt.game.graphics;

/*public class TextureManager {

    private static Texture[] generatedTextures;
    private static Texture[] loadedTextures;

    // MARK: - Public

    public static void loadAll() {
        generateTextures();
    }

    public static Texture getTexture(int texture) {
        return generatedTextures[texture];
    }

    // MARK: - Private

    private static void generateTextures() {
        generatedTextures = new Texture[5];

        generatedTextures[0] = new Texture((x, y) -> 65536 * 192 * ((x % 16) & (y % 16)));
        generatedTextures[1] = new Texture((x, y) ->
                (y * 128 / 64 + x * 128 / 64) +
                (1 << 8) * (y * 128 / 64 + x * 128 / 64) +
                (1 << 16) * (y * 128 / 64 + x * 128 / 64));
        generatedTextures[2] = new Texture((x, y) ->
                256 * (y * 128 / 64 + x * 128 / 64) + 65536 * (y * 128 / 64 + x * 128 / 64));
        generatedTextures[3] = new Texture((x, y) -> 256 * ((x * 256 / 64) ^ (y * 256 / 64)));
        generatedTextures[4] = new Texture((x, y) -> ((x ^ y) * 4) + ((x ^ y) * 262144));

    }

}*/
