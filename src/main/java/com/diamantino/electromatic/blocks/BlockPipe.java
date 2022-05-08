package com.diamantino.electromatic.blocks;

import com.diamantino.electromatic.api.multipart.IEMPartBlock;
import com.diamantino.electromatic.registration.EMBlocks;
import com.diamantino.electromatic.tiles.electronics.TilePipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * @author DiamantinoOp
 */
public class BlockPipe extends PipeBlock implements IEMPartBlock, EntityBlock {
    public BlockPipe() {
        super(0.25F, Properties.of(Material.PISTON).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.makeConnections(context.getLevel(), context.getClickedPos());
    }

    public BlockState makeConnections(BlockGetter world, BlockPos pos) {
        Block blockDown = world.getBlockState(pos.below()).getBlock();
        Block blockUp = world.getBlockState(pos.above()).getBlock();
        Block blockNorth = world.getBlockState(pos.north()).getBlock();
        Block blockEast = world.getBlockState(pos.east()).getBlock();
        Block blockSouth = world.getBlockState(pos.south()).getBlock();
        Block blockWest = world.getBlockState(pos.west()).getBlock();
        return this.defaultBlockState()
                .setValue(DOWN, blockDown == this)
                .setValue(UP, blockUp == this )
                .setValue(NORTH, blockNorth == this)
                .setValue(EAST, blockEast == this)
                .setValue(SOUTH, blockSouth == this )
                .setValue(WEST, blockWest == this);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.box(4, 4, 4, 12, 12, 12);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TilePipe(pos, state);
    }
}