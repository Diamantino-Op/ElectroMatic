package com.diamantino.electromatic.api.pipe;

import net.minecraft.core.Direction;

/**
 This interface is implemented by inventories with a buffer inventory, in which the pipe _can_ but doesn't prefer to
 insert items back into the buffer. An arbitrarily large number is returned, 1000000. A Restriction Pipe has a weight
 of 5000, a normal pipe 1.
 @author DiamantinoOp
 */
public interface IWeightedPipeInventory {

    /**
     By default this can be seen as 0 for non implementing inventories. return a high value to make it less prefered
     by the tubes.
     */
    public int getWeight(Direction from);

}