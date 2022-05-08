package com.diamantino.electromatic;

import com.diamantino.electromatic.api.EMApi;
import com.diamantino.electromatic.api.block.IAdvancedSilkyRemovable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ElectroMaticAPI implements EMApi.IEMApi {

    /*@Override
    public AlloyFurnaceRegistry getAlloyFurnaceRegistry() {

        return AlloyFurnaceRegistry.getInstance();
    }*/

    @Override
    public void loadSilkySettings(Level world, BlockPos pos, ItemStack stack) {
        BlockEntity te = world.getBlockEntity(pos);
        BlockState blockState = world.getBlockState(pos);
        if (te == null)
            throw new IllegalStateException("This block doesn't have a tile entity?!");
        if (stack.isEmpty())
            throw new IllegalArgumentException("ItemStack is empty!");
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag.contains("tileData")) {
                if (te instanceof IAdvancedSilkyRemovable) {
                    ((IAdvancedSilkyRemovable) te).readSilkyData(world, pos, tag.getCompound("tileData"));
                } else if (blockState.getBlock() instanceof IAdvancedSilkyRemovable) {
                    ((IAdvancedSilkyRemovable) blockState.getBlock()).readSilkyData(world, pos, tag.getCompound("tileData"));
                } else {
                    CompoundTag tileTag = tag.getCompound("tileData");
                    tileTag.putInt("x", pos.getX());
                    tileTag.putInt("y", pos.getY());
                    tileTag.putInt("z", pos.getZ());
                    te.load(tileTag);
                }
            }
        }
    }
}