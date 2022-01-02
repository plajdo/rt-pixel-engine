package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.serialization.Tag;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class GameMap {

    private Map walls;
    private Map walkable;

    // MARK: - Constructor

    public GameMap(String mapName, int width, int height) {
        walls = new Map(height, width);
        walkable = new Map(height, width);

        try {
            loadMaps(mapName);
        } catch(IOException e) {
            System.err.println("Could not load map due to exception: " + e.getLocalizedMessage());
        }
    }

    // MARK: - Public

    public Map getWallMap() {
        return walls;
    }

    // MARK: - Private

    private void loadMaps(String name) throws IOException {
        this.walls = loadMap(name + "_walls");
        this.walkable = loadMap(name + "_walkable");
    }

    private Map loadMap(String name) throws IOException {
        var inputStream = new FileInputStream("res/maps/" + name + ".map");
        byte[] mapBytes = inputStream.readAllBytes();

        inputStream.close();

        return Tag.fromBytes(mapBytes, Map.class).getContent();
    }

}
