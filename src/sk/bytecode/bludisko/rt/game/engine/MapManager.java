package sk.bytecode.bludisko.rt.game.engine;

import sk.bytecode.bludisko.rt.game.engine.serialization.Serializable;
import sk.bytecode.bludisko.rt.game.engine.serialization.Serializer;
import sk.bytecode.bludisko.rt.game.engine.serialization.tags.ObjectTag;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class MapManager {

    Map gameMap;

    public MapManager(String mapName) {
        System.out.println("savemap");

        gameMap = new Map();

        try {
            saveMap();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void loadMap() {

    }

    private void saveMap() {
        //var serializer = new Serializer();
        //serializer.serialize(7);

    }

}
