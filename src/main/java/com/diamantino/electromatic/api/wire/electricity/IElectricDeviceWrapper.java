package com.diamantino.electromatic.api.wire.electricity;

import net.minecraft.core.Direction;

public interface IElectricDeviceWrapper {

    public IElectricDevice getDeviceOnSide(Direction side);

}