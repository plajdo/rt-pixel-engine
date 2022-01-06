package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.serialization.Serializable;
import sk.bytecode.bludisko.rt.game.serialization.Tag;

import java.io.FileInputStream;
import java.io.IOException;

public class GameMap {

    @Serializable private final Map walls;
    @Serializable private final Map floor;

    @Serializable private final Vector2 spawnLocation;
    @Serializable private final Vector2 spawnDirection;

    // MARK: - Initialize

    public static GameMap load(String name) {
        try {
            var inputStream = new FileInputStream("res/maps/" + name + ".map");
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

    public GameMap(Map walls, Map floor, Vector2 spawnLocation, Vector2 spawnDirection) {
        this.walls = walls;
        this.floor = floor;
        this.spawnLocation = spawnLocation;
        this.spawnDirection = spawnDirection;
    }

    // MARK: - Public

    public Map walls() {
        return walls;
    }

    public Map floor() {
        return floor;
    }

    public Vector2 getSpawnLocation() {
        return spawnLocation;
    }

    public Vector2 getSpawnDirection() {
        return spawnDirection;
    }

}
