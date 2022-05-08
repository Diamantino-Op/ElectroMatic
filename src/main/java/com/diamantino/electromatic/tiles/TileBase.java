package com.diamantino.electromatic.tiles;

import com.diamantino.electromatic.blocks.BlockContainerFacingBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * @author DiamantinoOp
 */
public class TileBase extends BlockEntity implements IRotatable {

    private boolean isRedstonePowered = false;
    private int outputtingRedstone = 0;
    private int ticker = 0;

    public TileBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /*************** BASIC TE FUNCTIONS **************/

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void load(@NotNull CompoundTag tCompound) {

        super.load(tCompound);
        isRedstonePowered = tCompound.getBoolean("isRedstonePowered");
        readFromPacketNBT(tCompound);
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    protected void saveAdditional(@NotNull CompoundTag tCompound) {

        super.saveAdditional(tCompound);
        tCompound.putBoolean("isRedstonePowered", isRedstonePowered);
        writeToPacketNBT(tCompound);
    }

    /**
     * Tags written in here are synced upon markBlockForUpdate.
     *
     * @param tCompound
     */
    protected void writeToPacketNBT(CompoundTag tCompound) {
        tCompound.putByte("outputtingRedstone", (byte) outputtingRedstone);
    }

    protected void readFromPacketNBT(CompoundTag tCompound) {
        outputtingRedstone = tCompound.getByte("outputtingRedstone");
        if (level != null)
            markForRenderUpdate();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if(pkt.getTag() != null) {
            readFromPacketNBT(pkt.getTag());
            handleUpdateTag(pkt.getTag());
        }
    }

    protected void sendUpdatePacket() {

        if (!level.isClientSide)
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
    }

    protected void markForRenderUpdate() {

        if (level != null)
            level.setBlocksDirty(getBlockPos(), getBlockState(), getBlockState());
    }

    protected void notifyNeighborBlockUpdate() {

        //world.notifyBlocksOfNeighborChange(pos, getBlockType());
    }

    /**
     * ************** ADDED FUNCTIONS ****************
     */

    public static void setChanged(Level level, BlockPos pos, BlockState state, TileBase blockEntity) {

        checkRedstonePower(level, pos, state, blockEntity);
    }

    /**
     * Checks if redstone has changed.
     */
    public static void checkRedstonePower(Level level, BlockPos pos, BlockState state, TileBase blockEntity) {

        boolean isIndirectlyPowered = (level.getBestNeighborSignal(pos) != 0);
        if (isIndirectlyPowered && !blockEntity.getIsRedstonePowered()) {
            blockEntity.redstoneChanged(true);
        } else if (blockEntity.getIsRedstonePowered() && !isIndirectlyPowered) {
            blockEntity.redstoneChanged(false);
        }
    }

    /**
     * Before being able to use this, remember to mark the block as redstone emitter by calling BlockContainerBase#emitsRedstone()
     *
     * @param newValue
     */
    public void setOutputtingRedstone(boolean newValue) {

        setOutputtingRedstone(newValue ? 15 : 0);
    }

    /**
     * Before being able to use this, remember to mark the block as redstone emitter by calling BlockContainerBase#emitsRedstone()
     *
     * @param value
     */
    public void setOutputtingRedstone(int value) {

        value = Math.max(0, value);
        value = Math.min(15, value);
        if (outputtingRedstone != value) {
            outputtingRedstone = value;
            notifyNeighborBlockUpdate();
        }
    }

    public int getOutputtingRedstone() {

        return outputtingRedstone;
    }

    /**
     * This method can be overwritten to get alerted when the redstone level has changed.
     *
     * @param newValue
     *            The redstone level it is at now
     */
    protected void redstoneChanged(boolean newValue) {

        isRedstonePowered = newValue;
    }

    /**
     * Check whether or not redstone level is high
     */
    public boolean getIsRedstonePowered() {

        return isRedstonePowered;
    }

    /**
     * Returns the ticker of the Tile, this number wll increase every tick
     *
     * @return the ticker
     */
    public int getTicker() {

        return ticker;
    }

    /**
     * Gets called when the BlockEntity ticks for the first time, the world is accessible and updateEntity() has not been ran yet
     */
    protected static void onTileLoaded(Level level, BlockPos pos, BlockState state, TileBase blockEntity) {

        if (level != null && !level.isClientSide)
            setChanged(level, pos, state, blockEntity);
    }

    public NonNullList<ItemStack> getDrops() {

        return NonNullList.create();
    }

    @Override
    public void setFacingDirection(Direction dir) {
        if(getBlockState().getBlock() instanceof BlockContainerFacingBase) {
            BlockContainerFacingBase.setState(dir, level, getBlockPos());
            if (worldPosition != null) {
                sendUpdatePacket();
                notifyNeighborBlockUpdate();
            }
        }
    }

    @Override
    public Direction getFacingDirection() {
        if(getBlockState().getBlock() instanceof BlockContainerFacingBase) {
            return getBlockState().getValue(BlockContainerFacingBase.FACING);
        }
        return Direction.UP;
    }

    public boolean canConnectRedstone() {

        return false;
    }

    public static void tickTileBase(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        TileBase tileBase = (TileBase)blockEntity;
        if (tileBase.ticker == 0) {
            onTileLoaded(level, pos, state, tileBase);
        }
        tileBase.ticker++;
    }
}