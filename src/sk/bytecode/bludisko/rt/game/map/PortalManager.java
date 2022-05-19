package sk.bytecode.bludisko.rt.game.map;

import sk.bytecode.bludisko.rt.game.blocks.game.Portal;
import sk.bytecode.bludisko.rt.game.blocks.group.PortalPlaceable;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.util.Arrays;

public final class PortalManager {

    private PortalManager() {}

    /**
     * Force-creates a new pair of portals between given coordinates.
     * Equivalent to REST-POST.
     * @param map {@link GameMap} to create portals in
     * @param orangePortalCoordinates Coordinates of the {@link Portal.Color#ORANGE} portal
     * @param bluePortalCoordinates Coordinates of the {@link Portal.Color#BLUE} portal
     * @see Portal
     */
    public static void createPortal(GameMap map, Vector2 orangePortalCoordinates, Vector2 bluePortalCoordinates) {
        removeAllPortals(map);
        var walls = map.walls();

        var blocksAtOrangePortalCoordinates = walls.getBlocksAt(orangePortalCoordinates);
        var blocksAtBluePortalCoordinates = walls.getBlocksAt(bluePortalCoordinates);

        var orangePortalWall = Arrays.stream(blocksAtOrangePortalCoordinates)
                .filter(block -> block instanceof PortalPlaceable)
                .findFirst();

        var bluePortalWall = Arrays.stream(blocksAtBluePortalCoordinates)
                .filter(block -> block instanceof PortalPlaceable)
                .findFirst();

        var orangePortalSide = orangePortalWall.map(wall -> wall.getSide(orangePortalCoordinates));
        var bluePortalSide = bluePortalWall.map(wall -> wall.getSide(bluePortalCoordinates));

        var orangePortal = NullSafe.apply(
                orangePortalWall,
                orangePortalSide,
                (wall, side) -> new Portal(side, Portal.Color.ORANGE, wall.getCoordinates())
        );
        var bluePortal = NullSafe.apply(
                bluePortalWall,
                bluePortalSide,
                (wall, side) -> new Portal(side, Portal.Color.BLUE, wall.getCoordinates())
        );

        NullSafe.accept(orangePortalWall, orangePortal, walls::replaceBlock);
        NullSafe.accept(bluePortalWall, bluePortal, walls::replaceBlock);

        NullSafe.accept(orangePortal, bluePortal, (orange, blue) -> {
            orange.setOtherPortal(blue);
            blue.setOtherPortal(orange);
        });
    }

    /**
     * Creates a new portal or moves an existing one to a new location. Portal
     * is identified by its {@link Portal.Color}.
     * Equivalent to REST-PUT.
     * @param map {@link GameMap} to create the portal in
     * @param portalCoordinates Coordinates of the portal
     * @param portalColor Color of the portal
     */
    public static void createPortal(GameMap map, Vector2 portalCoordinates, Portal.Color portalColor) {
        var walls = map.walls();
        var portalBlocks = walls.getBlocksAt(portalCoordinates);

        var portalWall = Arrays.stream(portalBlocks)
                .filter(block -> block instanceof PortalPlaceable)
                .findFirst();

        var portalSide = portalWall.map(wall -> wall.getSide(portalCoordinates));

        var newPortal = NullSafe.apply(
                portalWall,
                portalSide,
                (wall, side) -> new Portal(side, portalColor, wall.getCoordinates())
        );

        var otherPortal = walls.blockStream()
                .filter(block -> block instanceof Portal)
                .map(block -> (Portal) block)
                .filter(portal -> portal.getColor() != portalColor)
                .findFirst();

        if(newPortal.isEmpty()) return;
        removePortal(map, portalColor);

        NullSafe.accept(portalWall, newPortal, walls::replaceBlock);
        NullSafe.accept(newPortal, otherPortal, Portal::setOtherPortal);
        NullSafe.accept(otherPortal, newPortal, Portal::setOtherPortal);
    }

    /**
     * Removes a portal of a given color from a map.
     * @param map {@link GameMap} to remove a portal from
     * @param portalColor {@link Portal.Color} of the portal
     */
    public static void removePortal(GameMap map, Portal.Color portalColor) {
        var walls = map.walls();
        var portalWall = walls.blockStream()
                .filter(block -> block instanceof Portal)
                .map(block -> (Portal) block)
                .filter(portal -> portal.getColor() == portalColor)
                .findFirst();

        portalWall.ifPresent(portal -> {
            portal.getOtherPortal().setOtherPortal(null);
            walls.replaceBlock(portal, portal.getOriginalWall());
        });
    }

    /**
     * Removes all portals from a {@link GameMap}.
     * @param map GameMap to remove all portals from
     */
    public static void removeAllPortals(GameMap map) {
        var walls = map.walls();
        walls.blockStream()
                .filter(block -> block instanceof Portal)
                .map(block -> (Portal) block)
                .forEach(portal -> walls.replaceBlock(portal, portal.getOriginalWall()));
    }

}
