package com.diamantino.electromatic.api.wire.electricity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface IElectricityProvider {

    /**
     * Returns the electric device at the specified coordinates and on the specified side and face.
     *
     * @param world
     *            The world where the device is
     * @param pos
     *            Location of the device.
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link null} if not know or not a face device
     * @return The electric device at the specified coords, side and face.
     */
    public IElectricDevice getElectricDeviceAt(Level world, BlockPos pos, Direction side, Direction face);

    /**
     * Returns the bundled device at the specified coordinates and on the specified side and face.
     *
     * @param world
     *            The world where the device is
     * @param pos
     *            Location of the device.
     * @param side
     *            Side of the device we're looking for
     * @param face
     *            Face the device must be placed on or {@link null} if not know or not a face device
     * @return The bundled device at the specified coords, side and face.
     */
    public IBundledDevice getBundledDeviceAt(Level world, BlockPos pos, Direction side, Direction face);



}