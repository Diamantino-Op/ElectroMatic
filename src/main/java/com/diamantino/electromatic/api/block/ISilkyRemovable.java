package com.diamantino.electromatic.api.block;

/**
 * This interface, when implemented by a block, will be called by a Silky Screwdriver upon right clicking.
 * It will get the TileEntity / parts, write the NBT to the item in the 'tileData' tag, and break the block/parts.
 *
 * Now make sure on block/parts placement to call EMApi.getInstance().loadSilkySettings(args) to load the tag back.
 * @author DiamantinoOp
 */
public interface ISilkyRemovable {
}