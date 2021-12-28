package sk.bytecode.bludisko.rt.game.map;

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
