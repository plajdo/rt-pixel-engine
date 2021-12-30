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
        Tag mapTag = new ObjectTag<>(new Map(30, 30));

        var fos = new FileOutputStream("res/maps/testMap2.map");
        fos.write(mapTag.byteData());

        fos.flush();
        fos.close();

    }

}
