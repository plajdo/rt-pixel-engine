package sk.bytecode.bludisko.rt.game.blocks.group;

import sk.bytecode.bludisko.rt.game.blocks.game.WhiteTiles;

/**
 * Interface defining all blocks a Portal can be placed on.
 * @see sk.bytecode.bludisko.rt.game.blocks.game.Portal
 */
public sealed interface PortalPlaceable permits WhiteTiles {}
