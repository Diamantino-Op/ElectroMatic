package com.diamantino.electromatic.items;

import com.diamantino.electromatic.api.multipart.IEMPartBlock;
import com.diamantino.electromatic.blocks.BlockEMMultipart;
import com.diamantino.electromatic.registration.EMBlocks;
import com.diamantino.electromatic.tiles.TileEMMultipart;
import com.diamantino.electromatic.utils.AABBUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * IEMPartBlock's use this rather then BlockItem for their Items.
 * @author DiamantinoOp
 */
public class ItemEMPart extends BlockItem {
    public ItemEMPart(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        BlockState thisState = getBlock().getStateForPlacement(context);

        if(state.getBlock() instanceof IEMPartBlock && thisState != null && !AABBUtils.testOcclusion(((IEMPartBlock)thisState.getBlock()).getOcclusionShape(thisState), state.getShape(context.getLevel(), context.getClickedPos()))) {

            //Save the Tile Entity Data
            CompoundTag nbt = new CompoundTag();
            BlockEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if(tileEntity != null){
                nbt = tileEntity.saveWithoutMetadata();
            }

            //Replace with Multipart
            context.getLevel().setBlockAndUpdate(context.getClickedPos(), EMBlocks.multipart.get().defaultBlockState());
            tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if(tileEntity instanceof TileEMMultipart){
                //Add the original State to the Multipart
                ((TileEMMultipart) tileEntity).addState(state);

                //Restore the Tile Entity Data
                BlockEntity tile = ((TileEMMultipart) tileEntity).getTileForState(state);
                if (tile != null)
                    tile.load(nbt);

                //Add the new State
                ((TileEMMultipart) tileEntity).addState(thisState);
                thisState.getBlock().setPlacedBy( context.getLevel(),context.getClickedPos(), thisState, context.getPlayer(), context.getItemInHand());
            }
            //Update Self
            state.neighborChanged(context.getLevel(), context.getClickedPos(), state.getBlock(), context.getClickedPos(), false);
            context.getItemInHand().shrink(1);
            //Place Sound
            context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;

        }else if(state.getBlock() instanceof BlockEMMultipart && thisState != null && !AABBUtils.testOcclusion(((IEMPartBlock)thisState.getBlock()).getOcclusionShape(thisState), state.getShape(context.getLevel(), context.getClickedPos()))) {

            // Add to the Existing Multipart
            BlockEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if (tileEntity instanceof TileEMMultipart) {
                ((TileEMMultipart) tileEntity).addState(thisState);
                thisState.getBlock().setPlacedBy( context.getLevel(),context.getClickedPos(), thisState, context.getPlayer(), context.getItemInHand());
                //Update Neighbors
                for(Direction dir : Direction.values()){
                    context.getLevel().getBlockState(context.getClickedPos().relative(dir)).neighborChanged(context.getLevel(), context.getClickedPos().relative(dir), state.getBlock(), context.getClickedPos(), false);
                }
                //Update Self
                state.neighborChanged(context.getLevel(), context.getClickedPos(), state.getBlock(), context.getClickedPos(), false);
                context.getItemInHand().shrink(1);
                //Place Sound
                context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

        }
        return super.place(context);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        return true;
    }
}