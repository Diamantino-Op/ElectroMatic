package com.diamantino.electromatic.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

/**
 * Implemented by blocks/parts which need more control over the Silky removing. Like setting a flag so inventory contents don't get dropped.
 *
 * @author DiamanyinoOp
 */
public interface IAdvancedSilkyRemovable extends ISilkyRemovable {

    /**
     *
     * @param world
     * @param pos
     * @return return false to prevent silky removing.
     */
    public boolean preSilkyRemoval(Level world, BlockPos pos);

    public void postSilkyRemoval(Level world, BlockPos pos);

    /**
     *
     * @param world
     * @param pos
     * @param tag
     * @return Return true if you want the "Has silky data" tooltip to be hidden
     */
    public boolean writeSilkyData(Level world, BlockPos pos, CompoundTag tag);

    public void readSilkyData(Level world, BlockPos pos, CompoundTag tag);
}