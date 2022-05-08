package com.diamantino.electromatic.api.pipe;

import net.minecraft.core.Direction;

/**
 *
 * @author DiamantinoOp
 */

public interface IPipeConnection {

    public boolean isConnectedTo(Direction from);

    /**
     *
     * @param stack PipeStack, as it needs to save the color if it bounced into the buffer.
     * @param from
     * @param simulate when true, only return what would have been accepted, but don't actually accept.
     * @return The PipeStack that was unable to enter this IPipeConnection
     */
    //public PipeStack acceptItemFromPipe(TubeStack stack, EnumFacing from, boolean simulate);
}