package com.diamantino.electromatic.blocks;

import com.diamantino.electromatic.api.block.IAdvancedSilkyRemovable;
import com.diamantino.electromatic.tiles.TileBase;
import com.diamantino.electromatic.tiles.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author DiamantinoOp
 */

public class BlockContainerBase extends BlockBase implements IAdvancedSilkyRemovable, EntityBlock {

    private Class<? extends TileBase> tileEntityClass;
    private boolean isRedstoneEmitter;
    private boolean isSilkyRemoving;

    public BlockContainerBase(Material material, Class<? extends TileBase> tileEntityClass, BlockEntityType<? extends TileBase> entityType) {
        super(material);
        setBlockEntityClass(tileEntityClass);
    }

    public BlockContainerBase(Properties properties, Class<? extends TileBase> tileEntityClass, BlockEntityType<? extends TileBase> entityType) {
        super(properties);
        setBlockEntityClass(tileEntityClass);
    }

    public BlockContainerBase setBlockEntityClass(Class<? extends TileBase> tileEntityClass) {

        this.tileEntityClass = tileEntityClass;
        return this;
    }

    public BlockContainerBase emitsRedstone() {

        isRedstoneEmitter = true;
        return this;
    }


    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootContext.@NotNull Builder builder) {
        List<ItemStack> drops =  super.getDrops(state, builder);
        drops.add(new ItemStack(this));
        return drops;
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof TileBase) {
                Containers.dropContents(worldIn, pos, ((TileBase)tileentity).getDrops());
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    /**
     * Fetches the BlockEntity Class that goes with the block
     *
     * @return a .class
     */
    protected Class<? extends BlockEntity> getBlockEntity() {

        return tileEntityClass;
    }

    protected TileBase get(BlockGetter w, BlockPos pos) {

        BlockEntity te = w.getBlockEntity(pos);

        if (!(te instanceof TileBase))
            return null;

        return (TileBase) te;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        try {
            return getBlockEntity().getDeclaredConstructor(BlockPos.class, BlockState.class).newInstance(pos, state);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TileMachineBase::tickMachineBase;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        // Only do this on the server side.
        if (!world.isClientSide) {
            TileBase tileEntity = get(world, pos);
            if (tileEntity != null) {
                TileBase.setChanged(world, pos, state, tileEntity);
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return isRedstoneEmitter;
    }

    @Override
    public int getSignal(@NotNull BlockState blockState, @NotNull BlockGetter par1BlockGetter, BlockPos pos, Direction side) {
        TileBase te = get(par1BlockGetter, pos);
        if (te != null) {
            return te.getOutputtingRedstone();
        }
        return 0;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult rayTraceResult) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        //TODO: Fix

        /*if (player.isCrouching()) {
            if (!player.getItemInHand(hand).isEmpty()) {
                if (player.getItemInHand(hand).getItem() == EMItems.screwdriver.get()) {
                    return InteractionResult.FAIL;
                }
            }
        }*/
        if (player.isCrouching()) {
            return InteractionResult.FAIL;
        }
        if (world.getBlockEntity(pos) instanceof MenuProvider) {
            NetworkHooks.openGui((ServerPlayer) player, (MenuProvider)world.getBlockEntity(pos));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    protected boolean canRotateVertical() {

        return true;
    }


    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean preSilkyRemoval(Level world, BlockPos pos) {

        isSilkyRemoving = true;
        return true;
    }

    @Override
    public void postSilkyRemoval(Level world, BlockPos pos) {

        isSilkyRemoving = false;
    }

    @Override
    public boolean writeSilkyData(Level world, BlockPos pos, CompoundTag tag) {

        Objects.requireNonNull(world.getBlockEntity(pos)).saveWithFullMetadata();
        return false;
    }

    @Override
    public void readSilkyData(Level world, BlockPos pos, CompoundTag tag) {
        Objects.requireNonNull(world.getBlockEntity(pos)).load(tag);
    }

}