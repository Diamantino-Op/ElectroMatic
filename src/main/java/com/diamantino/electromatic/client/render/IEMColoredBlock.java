package com.diamantino.electromatic.client.render;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author DiamantinoOp
 */
public interface IEMColoredBlock {

    int getColor(BlockState state, BlockGetter w, BlockPos pos, int tint);
    int getColor(ItemStack stack, int tint);

}