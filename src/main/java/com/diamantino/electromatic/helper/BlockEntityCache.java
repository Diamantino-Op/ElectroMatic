package com.diamantino.electromatic.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 *
 * @author DiamantinoOp
 */

public class BlockEntityCache extends LocationCache<BlockEntity> {

    public BlockEntityCache(Level world, BlockPos pos) {

        super(world, pos);
    }

    @Override
    protected BlockEntity getNewValue(Level world, BlockPos pos, Object... extraArgs) {

        return world.getBlockEntity(pos);
    }

}