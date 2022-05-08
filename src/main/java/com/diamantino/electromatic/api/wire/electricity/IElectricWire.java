package com.diamantino.electromatic.api.wire.electricity;

import net.minecraft.core.Direction;

public interface IElectricWire extends IElectricDevice, IElectricConductor.IAdvancedElectricConductor,
        IBundledConductor.IAdvancedBundledConductor {

    public ElectricWireType getRedwireType(Direction side);

}