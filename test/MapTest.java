package test;

import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.serialization.ObjectTag;
import sk.bytecode.bludisko.rt.game.serialization.Tag;

import java.io.FileOutputStream;
import java.io.IOException;

public class MapTest {

    @Test
    void saveMap() throws IOException {

        var map = new Map(30, 30);
        map.setTile(5, 2, 1);

        Tag mapTag = new ObjectTag<>(map);

        var fos = new FileOutputStream("res/maps/testMap2_walls.map");
        fos.write(mapTag.byteData());

        fos.flush();
        fos.close();

    }

}
