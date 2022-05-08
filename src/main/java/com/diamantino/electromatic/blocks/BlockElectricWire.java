package com.diamantino.electromatic.blocks;

import com.diamantino.electromatic.api.misc.MinecraftColor;
import com.diamantino.electromatic.api.wire.electricity.CapabilityElectricDevice;
import com.diamantino.electromatic.api.wire.electricity.ElectricWireType;
import com.diamantino.electromatic.client.render.IEMColoredBlock;
import com.diamantino.electromatic.tiles.TileEMMultipart;
import com.diamantino.electromatic.tiles.electronics.TileElectricWire;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockElectricWire extends BlockEMWireBase implements IEMColoredBlock, EntityBlock {
    final String type;
    final MinecraftColor color;

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileElectricWire(pos, state);
    }

    @Override
    protected Capability<?> getCapability() {
        return CapabilityElectricDevice.ELECTRICITY_CAPABILITY;
    }

    public BlockElectricWire(String type, MinecraftColor color) {
        super(1,2F);
        this.type = type;
        this.color = color;

        //setRegistryName(References.MOD_ID + ":" + MinecraftColor.getName(color) + "_" + type + "_wire");
    }

    public BlockElectricWire(String type, MinecraftColor color, float width, float height) {
        super(width, height);
        this.type = type;
        this.color = color;
    }

    @Override
    protected boolean canConnect(Level world, BlockPos pos, BlockState state, BlockEntity tileEntity, Direction direction) {
        if(state.getBlock().canConnectRedstone(state, world, pos, direction))
            return true;
        return super.canConnect(world, pos, state, tileEntity, direction);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TileElectricWire::tickCable;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, JOIN_FRONT, JOIN_BACK, JOIN_LEFT, JOIN_RIGHT, WATERLOGGED);
    }

    @Override
    public int getColor(BlockState state, BlockGetter world, BlockPos pos, int tintIndex) {
        //Color for Block
        BlockEntity tile = (world.getBlockEntity(pos));
        if(tile instanceof TileEMMultipart){
            tile = ((TileEMMultipart)tile).getTileForState(state);
        }
        if(tile instanceof TileElectricWire && tintIndex == 1) {
            tile.getCapability(CapabilityElectricDevice.ELECTRICITY_CAPABILITY).orElse(null).setInsulationColor(color);
        }
        return color.getHex();
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        //Color for Block
        /*MinecraftColor color = MinecraftColor.BLUE;
        if(stack.getTag() != null && stack.getTag().contains("color"))
            color = MinecraftColor.getColorFromString(stack.getTag().getString("color"));*/
        return tintIndex == 1 ? color.getHex() : tintIndex == 2 ? ElectricWireType.getTypeFromName(type).getColor() : -1;
    }

}
