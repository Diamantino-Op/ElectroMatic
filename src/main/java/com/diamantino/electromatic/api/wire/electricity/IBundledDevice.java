package com.diamantino.electromatic.api.wire.electricity;

import com.diamantino.electromatic.api.connect.ConnectionType;
import com.diamantino.electromatic.api.connect.IConnectionCache;
import com.diamantino.electromatic.api.misc.MinecraftColor;
import net.minecraft.core.Direction;

public interface IBundledDevice {

    /**
     * Returns whether or not the device passed as an argument can be connected to this device on the specified side. It also takes a ConnectionType,
     * which determines the type of connection to this device.
     */
    public boolean canConnect(Direction side, IBundledDevice dev, ConnectionType type);

    /**
     * Returns a cache of all the connections of other devices with this one. Create an instance of this class by calling
     * {@link IElectricityApi#createBundledConnectionCache(IBundledDevice)}
     */
    public IConnectionCache<? extends IBundledDevice> getBundledConnectionCache();

    /**
     * Gets the output of this device on the specified side.
     */
    public Electricity[] getBundledOutput(Direction side);

    /**
     * Sets the power level on the specified side to a set power level.
     */
    public void setBundledPower(Direction side, Electricity[] power);

    /**
     * Gets the input of this device on the specified side.
     */
    public Electricity[] getBundledPower(Direction side);

    /**
     * Notifies the device of a power change. (Usually called after propagation)
     */
    public void onBundledUpdate();

    /**
     * Gets the color of this bundled device. Normally used to determine if other blocks should connect to it.
     */
    public MinecraftColor getBundledColor(Direction side);

    /**
     * Returns whether or not this is a normal face (if face devices should be able to connect to it)
     */
    public boolean isNormalFace(Direction side);

}