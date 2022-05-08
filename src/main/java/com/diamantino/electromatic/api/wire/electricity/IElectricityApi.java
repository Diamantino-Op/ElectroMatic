package com.diamantino.electromatic.api.wire.electricity;

import com.diamantino.electromatic.api.connect.ConnectionType;
import com.diamantino.electromatic.api.connect.IConnection;
import com.diamantino.electromatic.api.connect.IConnectionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface IElectricityApi {

    /**
     * Returns the electric device at the specified coordinates and on the specified side and face. Data gotten from the registered
     * {@link IElectricityProvider}s
     *
     * @param world
     *            The world where the device is
     * @param pos
     *            Coordinate of the device
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link null} if not know or not a face device
     * @return The electric device at the specified coords, side and face.
     */
    public IElectricDevice getElectricDevice(Level world, BlockPos pos, Direction face, Direction side);

    /**
     * Returns the bundled device at the specified coordinates and on the specified side and face. Data gotten from the registered
     * {@link IElectricityProvider}s
     *
     * @param world
     *            The world where the device is
     * @param pos
     *            coordinate of the device
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link null} if not know or not a face device
     * @return The electric device at the specified coords, side and face.
     */
    public IBundledDevice getBundledDevice(Level world, BlockPos pos, Direction face, Direction side);

    /**
     * Registers a electric/bundled device provider.
     */
    public void registerElectricityProvider(IElectricityProvider provider);

    /**
     * Returns whether or not wires should output power.
     */
    public boolean shouldWiresOutputPower(boolean lossy);

    /**
     * Determines whether or not wires should output power.
     */
    public void setWiresOutputPower(boolean shouldWiresOutputPower, boolean lossy);

    /**
     * Returns whether or not wires should handle block/tile/part updates.
     */
    public boolean shouldWiresHandleUpdates();

    /**
     * Determines whether or not wires should handle block/tile/part updates.
     *
     * @param shouldWiresHandleUpdates
     */
    public void setWiresHandleUpdates(boolean shouldWiresHandleUpdates);

    public IConnection<IElectricDevice> createConnection(IElectricDevice a, IElectricDevice b, Direction sideA, Direction sideB,
                                                         ConnectionType type);

    public IConnection<IBundledDevice> createConnection(IBundledDevice a, IBundledDevice b, Direction sideA, Direction sideB,
                                                        ConnectionType type);

    public IConnectionCache<IElectricDevice> createElectricConnectionCache(IElectricDevice dev);

    public IConnectionCache<IBundledDevice> createBundledConnectionCache(IBundledDevice dev);

    public IPropagator<IElectricDevice> getElectricPropagator(IElectricDevice device, Direction side);

    public IPropagator<IBundledDevice> getBundledPropagator(IBundledDevice device, Direction side);

}