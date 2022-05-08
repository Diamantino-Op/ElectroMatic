package com.diamantino.electromatic.api.wire.electricity;

import net.minecraft.core.Direction;

public interface IBundledDeviceWrapper {

    public IBundledDevice getBundledDeviceOnSide(Direction side);

}