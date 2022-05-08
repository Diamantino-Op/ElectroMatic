package com.diamantino.electromatic.api.gate;

import com.diamantino.electromatic.api.wire.electricity.IBundledDevice;
import com.diamantino.electromatic.api.wire.electricity.IElectricDevice;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface IGateConnection {

    public IGate<?, ?, ?, ?, ?, ?> getGate();

    public Direction getDirection();

    public void notifyUpdate();

    public boolean isEnabled();

    public IGateConnection setEnabled(boolean enabled);

    public IGateConnection enable();

    public IGateConnection disable();

    public IGateConnection setOutputOnly();

    public IGateConnection setBidirectional();

    public boolean isOutputOnly();

    public void refresh();

    public void writeToNBT(CompoundTag tag);

    public void readFromNBT(CompoundTag tag);

    public void writeData(DataOutput buffer) throws IOException;

    public void readData(DataInput buffer) throws IOException;

    public boolean canConnectRedstone();

    public boolean canConnect(IElectricDevice device);

    public boolean canConnect(IBundledDevice device);

    public boolean isBundled();

    public byte getRedstoneOutput();

    public byte[] getBundledOutput();

    public void setRedstonePower(byte power);

    public void setBundledPower(byte[] power);

}