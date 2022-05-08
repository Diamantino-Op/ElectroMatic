package com.diamantino.electromatic.api.wire.electricity;

import net.minecraft.core.Direction;

public interface IRedConductor {

    public boolean hasLoss(Direction side);

    public boolean isAnalogue(Direction side);

}