package com.diamantino.electromatic.tiles.electronics;

import com.diamantino.electromatic.api.misc.MinecraftColor;
import com.diamantino.electromatic.api.wire.electricity.*;
import com.diamantino.electromatic.blocks.BlockEMCableBase;
import com.diamantino.electromatic.blocks.BlockElectricWire;
import com.diamantino.electromatic.client.render.IEMColoredBlock;
import com.diamantino.electromatic.registration.EMBlockEntityTypes;
import com.diamantino.electromatic.tiles.TileBase;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileElectricWire extends TileBase {
    private final IElectricDevice device = new ElectricityStorage();
    @Nullable
    private LazyOptional<IElectricDevice> electricityCap;

    public static final ModelProperty<Pair<Integer, Integer>> COLOR_INFO = new ModelProperty<>();

    public TileElectricWire(BlockPos pos, BlockState state) {
        super(EMBlockEntityTypes.wire.get(), pos, state);

        //setColor(MinecraftColor.WHITE);
    }

    public TileElectricWire(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        //setColor(MinecraftColor.WHITE);
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public IModelData getModelData(BlockState state) {

        //Add Color and Light Data
        Pair<Integer, Integer> colorData = Pair.of(((IEMColoredBlock)state.getBlock()).getColor(state, level, worldPosition, -1), ((IEMColoredBlock)state.getBlock()).getColor(state, level, worldPosition, 2));

        return new ModelDataMap.Builder().withInitial(COLOR_INFO, colorData).build();

    }

    public MinecraftColor getColor() {
        return device.getInsulationColor(null);
    }

    public boolean setColor(MinecraftColor color) {
        if(device.getInsulationColor(null) != color){
            device.setInsulationColor(color);
            return true;
        }
        return false;
    }

    @Override
    protected void readFromPacketNBT(CompoundTag tCompound) {
        super.readFromPacketNBT(tCompound);

        if(tCompound.contains("device")) {
            Tag nbtstorage = tCompound.get("device");
            IElectricDevice.readNBT(CapabilityElectricDevice.ELECTRICITY_CAPABILITY, device, null, nbtstorage);
        }

        /*if(tCompound.contains("color")) {
            device.setInsulationColor(MinecraftColor.getColorFromString(tCompound.getString("color")));
        }*/
    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);

        Tag nbtstorage = IElectricDevice.writeNBT(CapabilityElectricDevice.ELECTRICITY_CAPABILITY, device, null);
        tCompound.put("device", nbtstorage);
        //tCompound.putString("color", device.getInsulationColor(null).toString());
    }

    @Override
    public void load(@NotNull CompoundTag tCompound) {

        super.load(tCompound);
        /*if(tCompound.contains("color")) {
            device.setInsulationColor(MinecraftColor.getColorFromString(tCompound.getString("color")));
        }*/
        readFromPacketNBT(tCompound);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tCompound) {

        super.saveAdditional(tCompound);
        //tCompound.putString("color", device.getInsulationColor(null).toString());
        writeToPacketNBT(tCompound);
    }

    public static void tickCable(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof TileElectricWire tileCable) {

            if (level != null && !level.isClientSide) {
                if (state.getBlock() instanceof BlockElectricWire) {
                    List<Direction> directions = new ArrayList<>(BlockElectricWire.FACING.getPossibleValues());

                    //Check the side has capability
                    directions.removeIf(d -> !tileCable.getCapability(CapabilityElectricDevice.ELECTRICITY_CAPABILITY, d).isPresent());

                    for (Direction facing : directions) {
                        Block fBlock = level.getBlockState(pos.relative(facing)).getBlock();
                        if (fBlock != Blocks.AIR && fBlock != Blocks.WATER) {
                            BlockEntity tile = level.getBlockEntity(pos.relative(facing));
                            if (tile != null) {
                                if (tile.getCapability(CapabilityElectricDevice.ELECTRICITY_CAPABILITY, facing.getOpposite()).isPresent()) {
                                    IElectricDevice cap  = tile.getCapability(CapabilityElectricDevice.ELECTRICITY_CAPABILITY).orElse(null);

                                    IElectricDevice thisCap = tileCable.getCapability(CapabilityElectricDevice.ELECTRICITY_CAPABILITY).orElse(null);

                                    if (cap.getChange().hasChanged) {
                                        if (!(cap.getChange().changeSide == facing.getOpposite())) {
                                            if (cap.getChange().type == Change.ChangeType.ADDED) {
                                                Electricity elect = cap.getElectricityValue(facing.getOpposite());

                                                thisCap.setElectricityValue(null, new Electricity(elect.getVoltage() + cap.getChange().volatageChange, elect.getAmperage() + cap.getChange().amperageChange));

                                                thisCap.setChange(new Change(cap.getChange().volatageChange, cap.getChange().amperageChange, facing, Change.ChangeType.ADDED, true));

                                                cap.getChange().resetChange();
                                            } else {
                                                Electricity elect = cap.getElectricityValue(facing.getOpposite());

                                                thisCap.setElectricityValue(null, new Electricity(elect.getVoltage() - cap.getChange().volatageChange, elect.getAmperage() - cap.getChange().amperageChange));

                                                thisCap.setChange(new Change(cap.getChange().volatageChange, cap.getChange().amperageChange, facing, Change.ChangeType.REMOVED, true));

                                                cap.getChange().resetChange();
                                            }
                                        }
                                    }
                                }
                            }

                        } else {
                            BlockEntity tile = level.getBlockEntity(pos.relative(facing).relative(state.getValue(BlockElectricWire.FACING).getOpposite()));
                            if (tile != null) {
                                if (tile.getCapability(CapabilityElectricDevice.ELECTRICITY_CAPABILITY, state.getValue(BlockElectricWire.FACING)).isPresent()) {
                                    IElectricDevice cap  = tile.getCapability(CapabilityElectricDevice.ELECTRICITY_CAPABILITY).orElse(null);

                                    IElectricDevice thisCap = tileCable.getCapability(CapabilityElectricDevice.ELECTRICITY_CAPABILITY).orElse(null);

                                    if (cap.getChange().hasChanged) {
                                        if (!(cap.getChange().changeSide == facing.getOpposite())) {
                                            if (cap.getChange().type == Change.ChangeType.ADDED) {
                                                Electricity elect = cap.getElectricityValue(facing.getOpposite());

                                                thisCap.setElectricityValue(null, new Electricity(elect.getVoltage() + cap.getChange().volatageChange, elect.getAmperage() + cap.getChange().amperageChange));

                                                thisCap.setChange(new Change(cap.getChange().volatageChange, cap.getChange().amperageChange, facing, Change.ChangeType.ADDED, true));

                                                cap.getChange().resetChange();
                                            } else {
                                                Electricity elect = cap.getElectricityValue(facing.getOpposite());

                                                thisCap.setElectricityValue(null, new Electricity(elect.getVoltage() - cap.getChange().volatageChange, elect.getAmperage() - cap.getChange().amperageChange));

                                                thisCap.setChange(new Change(cap.getChange().volatageChange, cap.getChange().amperageChange, facing, Change.ChangeType.REMOVED, true));

                                                cap.getChange().resetChange();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        List<Direction> directions = new ArrayList<>(BlockEMCableBase.FACING.getPossibleValues());
        if(level != null) {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockElectricWire) {
                //Remove upward connections
                directions.remove(state.getValue(BlockElectricWire.FACING));

                //Make sure the cable is on the same side of the block
                directions.removeIf(d -> level.getBlockState(worldPosition.relative(d)).getBlock() instanceof BlockElectricWire
                        && level.getBlockState(worldPosition.relative(d)).getValue(BlockElectricWire.FACING) != state.getValue(BlockElectricWire.FACING));


                //Make sure the cable is the same color or none
                if(device.getInsulationColor(null) != MinecraftColor.NONE) {
                    directions.removeIf(d -> {
                        BlockEntity tile = level.getBlockEntity(worldPosition.relative(d));
                        return tile instanceof TileElectricWire && !(((TileElectricWire) tile).device.getInsulationColor(d) == device.getInsulationColor(d.getOpposite()) || ((TileElectricWire) tile).device.getInsulationColor(d) == MinecraftColor.NONE);
                    });
                }
            }
        }

        if(cap == CapabilityElectricDevice.ELECTRICITY_CAPABILITY && (side == null || directions.contains(side))){
            if( electricityCap == null ) electricityCap = LazyOptional.of( () -> device );
            return electricityCap.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps(){
        super.invalidateCaps();

        if( electricityCap != null )
        {
            electricityCap.invalidate();
            electricityCap = null;
        }
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        if (pkt.getTag() != null) {
            handleUpdateTag(pkt.getTag());
        }
    }
}
