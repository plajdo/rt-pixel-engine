import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.serialization.ObjectTag;
import sk.bytecode.bludisko.rt.game.serialization.Tag;

import java.io.FileOutputStream;
import java.io.IOException;

public class MapTest {

    @Test
    void saveMap() throws IOException {

        Integer[][] map = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 2, 2, 2, 1, 1, 1, 2, 0, 0, 0, 0, 1},
                {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {2, 0, 0, 5, 0, 0, 0, 0, 6, 0, 8, 0, 0, 0, 0, 1},
                {2, 0, 0, 6, 5, 0, 0, 5, 0, 7, 0, 0, 0, 0, 0, 1},
                {2, 3, 0, 7, 6, 5, 0, 0, 6, 0, 8, 0, 0, 0, 0, 1},
                {2, 3, 0, 8, 7, 6, 0, 5, 0, 7, 0, 0, 0, 0, 0, 1},
                {2, 3, 0, 0, 8, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {2, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 0, 1},
                {0, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2}
        };

        var mapObj = new Map(map);

        Tag<Map> mapTag = new ObjectTag<>(mapObj);

        var fos = new FileOutputStream("res/maps/testMap3_walls.map");
        fos.write(mapTag.byteData());

        fos.flush();
        fos.close();

    }

}
