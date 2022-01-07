package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.serialization.Serializable;
import sk.bytecode.bludisko.rt.game.serialization.Tag;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Map of a game world. Contains separate Map objects
 * holding information about walls, floor tiles, spawn
 * location, ...
 */
public final class GameMap {

    @Serializable private final Map walls;
    @Serializable private final Map floor;

    @Serializable private final Vector2 spawnLocation;
    @Serializable private final Vector2 spawnDirection;

    @Serializable private final int ceilingColor;

    // MARK: - Static Initializer

    /**
     * Loads a GameMap from a file inside resources (res/maps) folder.
     * @param name Map name without extension
     * @return GameMap loaded from the file
     * @throws RuntimeException If the map could not be loaded for any reason.
     */
    public static GameMap load(String name) {
        try {
            FileInputStream inputStream = new FileInputStream("res/maps/" + name + ".map");
            byte[] mapBytes = inputStream.readAllBytes();
            inputStream.close();

            var gameMap = Tag.fromBytes(mapBytes, GameMap.class).getContent();
            gameMap.walls.generateObjects();
            gameMap.floor.generateObjects();

            return gameMap;
        } catch(IOException e) {
            throw new RuntimeException("Could not load map!\nOriginal exception: " + e.getLocalizedMessage());
        }
    }

    // MARK: - Constructor

    /**
     * Creates a map from separate objects. Mainly used for map creation
     * and not meant to be used inside a game.
     */
    public GameMap(Map walls, Map floor, Vector2 spawnLocation, Vector2 spawnDirection, int ceilingColor) {
        this.walls = walls;
        this.floor = floor;
        this.spawnLocation = spawnLocation;
        this.spawnDirection = spawnDirection;
        this.ceilingColor = ceilingColor;
    }

    // MARK: - Getters

    /**
     * @return Map containing walls and 3D objects.
     */
    public Map walls() {
        return walls;
    }

    /**
     * @return Map containing floor tiles.
     */
    public Map floor() {
        return floor;
    }

    public Vector2 getSpawnLocation() {
        return spawnLocation;
    }

    public Vector2 getSpawnDirection() {
        return spawnDirection;
    }

    public int getCeilingColor() {
        return ceilingColor;
    }

}
