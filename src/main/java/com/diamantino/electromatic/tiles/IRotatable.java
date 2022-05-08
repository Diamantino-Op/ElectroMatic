package com.diamantino.electromatic.tiles;

import net.minecraft.core.Direction;

/**
 * @author DiamantinoOp
 */
public interface IRotatable {

    public void setFacingDirection(Direction dir);

    public Direction getFacingDirection();
}