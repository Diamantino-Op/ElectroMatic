package com.diamantino.electromatic.api.multipart;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Blocks that can be used as part of a multipart should implement this.
 * @author DiamantinoOp
 */
public interface IEMPartBlock {

    /**
     * If this Part should block a given capability returns true.
     * For example covers block Cables, Redstone and Items where as hollow covers allow Items.
     * @param state
     * @param capability
     * @param side
     */
    default Boolean blockCapability (BlockState state, Capability capability, @Nullable Direction side){
        return false;
    }

    /**
     * Return the occluding area given the BlockState, where other parts cannot be placed.
     * @param state
     */
    VoxelShape getOcclusionShape (BlockState state);

    /**
     * Separate onRemove for Multipart so that the BlockEntity isn't removed.
     * @param state
     * @param worldIn
     * @param pos
     * @param newState
     */
    default void onMultipartReplaced(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving){
    }

}