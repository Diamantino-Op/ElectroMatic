package com.diamantino.electromatic.api.wire.electricity;

import com.diamantino.electromatic.api.connect.ConnectionType;
import com.diamantino.electromatic.api.connect.IConnectionCache;
import com.diamantino.electromatic.api.misc.MinecraftColor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;

public interface IElectricDevice {

    /**
     * Returns whether or not the device passed as an argument can be connected to this device on the specified side. It also takes a ConnectionType,
     * which determines the type of connection to this device.
     */
    public boolean canConnect(Direction side, IElectricDevice dev, ConnectionType type);

    /**
     * Returns a cache of all the connections of other devices with this one. Create an instance of this class by calling
     * {@link IElectricityApi#createElectricConnectionCache(IElectricDevice)}
     */
    public IConnectionCache<? extends IElectricDevice> getElectricConnectionCache();

    /**
     * Gets the output of this device on the specified side.
     */
    public Electricity getElectricityValue(Direction side);

    /**
     * Sets the power level on the specified side to a set power level.
     */
    public void setElectricityValue(Direction side, Electricity electricity);

    /**
     * Gets the insulation color on the specified side. This usually determines whether or not things can connect to it.
     */
    public MinecraftColor getInsulationColor(Direction side);

    /**
     * Sets the insulation color only for initialisation.
     */
    public void setInsulationColor(MinecraftColor color);

    /**
     * Gets the change of the power of the device.
     */
    public Change getChange();

    /**
     * Sets the change of the power of the device.
     */
    public void setChange(Change changed);

    /**
     * Notifies the device of a power change. (Usually called after propagation)
     */
    public void onElectricityUpdate();

    /**
     * Returns whether or not this is a full face (if face devices should be able to connect to it)
     */
    public boolean isNormalFace(Direction side);

    public static Tag writeNBT(Capability<IElectricDevice> capability, IElectricDevice instance, Direction direction) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("color", instance.getInsulationColor(direction).name());
        nbt.putFloat("voltage", instance.getElectricityValue(direction).getVoltage());
        nbt.putFloat("amperage", instance.getElectricityValue(direction).getAmperage());
        return nbt;
    }

    public static void readNBT(Capability<IElectricDevice> capability, IElectricDevice instance, Direction side, Tag nbt) {
        CompoundTag tags = (CompoundTag) nbt;
        float voltage = tags.getFloat("voltage");
        float amperage = tags.getFloat("amperage");
        instance.setInsulationColor(MinecraftColor.valueOf(tags.getString("color")));
        instance.setElectricityValue(side, new Electricity(voltage, amperage));
    }

}