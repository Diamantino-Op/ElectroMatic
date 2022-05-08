package com.diamantino.electromatic.blocks;

import com.diamantino.electromatic.api.multipart.IEMPartBlock;
import com.diamantino.electromatic.registration.EMBlocks;
import com.diamantino.electromatic.tiles.TileEMMicroblock;
import com.diamantino.electromatic.tiles.TileEMMultipart;
import com.diamantino.electromatic.utils.AABBUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEMMicroblock extends BaseEntityBlock implements IEMPartBlock, SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final VoxelShape size;

    public BlockEMMicroblock(VoxelShape size) {
        super(Properties.of(Material.STONE).noOcclusion().strength(2));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
        this.size = size;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        BlockEntity tileentity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
        List<ItemStack> itemStacks = new ArrayList<>();
        if(tileentity instanceof TileEMMultipart){
            tileentity = ((TileEMMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileEMMicroblock) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("block", ((TileEMMicroblock)tileentity).getBlock().getRegistryName().toString());
            ItemStack stack = new ItemStack(this);
            stack.setTag(nbt);
            stack.setHoverName(new TranslatableComponent(((TileEMMicroblock)tileentity).getBlock().getDescriptionId())
                    .append(new TextComponent(" "))
                    .append(new TranslatableComponent(this.getDescriptionId())));
            itemStacks.add(stack);
        }
        return itemStacks;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        BlockEntity tileentity = world.getBlockEntity(pos);
        ItemStack stack = ItemStack.EMPTY;
        if(tileentity instanceof TileEMMultipart){
            tileentity = ((TileEMMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileEMMicroblock) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("block", ((TileEMMicroblock) tileentity).getBlock().getRegistryName().toString());
            stack = new ItemStack(this);
            stack.setTag(nbt);
            stack.setHoverName(new TranslatableComponent(((TileEMMicroblock) tileentity).getBlock().getDescriptionId())
                    .append(new TextComponent(" "))
                    .append(new TranslatableComponent(this.getDescriptionId())));
        }
        return stack;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AABBUtils.rotate(size, state.getValue(FACING));
    }

    public VoxelShape getSize() {
        return size;
    }

    @Override
    public Boolean blockCapability(BlockState state, Capability capability, @Nullable Direction side) {
        return side == state.getValue(FACING).getOpposite();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        AABB aabb = size.bounds();
        return AABBUtils.rotate(Block.box(3, aabb.minY * 16, 3, 13, aabb.maxY, 13), state.getValue(FACING));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }


    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        if(player != null && !player.isCrouching()) {
            return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        }
        Vec3 vec = context.getPlayer().getLookAngle();
        return this.defaultBlockState().setValue(FACING, Direction.getNearest(vec.x, vec.y, vec.z)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEMMicroblock(pos, state);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileEMMicroblock && stack.hasTag() && stack.getTag().contains("block")) {
            //Update Microblock Type based on Stack
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stack.getTag().getString("block")));
            ((TileEMMicroblock) tileentity).setBlock(block);
        }else if(tileentity instanceof TileEMMultipart && stack.hasTag() && stack.getTag().contains("block")){
            //Update Multipart Microblock Type based on Stack
            TileEMMicroblock tile = (TileEMMicroblock)((TileEMMultipart)tileentity).getTileForState(state);
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stack.getTag().getString("block")));
            tile.setBlock(block);
        }
    }

}