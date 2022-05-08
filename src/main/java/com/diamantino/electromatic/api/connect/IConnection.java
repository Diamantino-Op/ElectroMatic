package com.diamantino.electromatic.api.connect;

import net.minecraft.core.Direction;

public interface IConnection<T> {

    /**
     * Returns the first device.
     */
    public T getA();

    /**
     * Returns the second device.
     */
    public T getB();

    /**
     * Returns the side of the first device this connection is on.
     */
    public Direction getSideA();

    /**
     * Returns the side of the second device this connection is on.
     */
    public Direction getSideB();

    /**
     * Returns the type of connection.
     */
    public ConnectionType getType();

    /**
     * Gets the connection from B's point of view.
     */
    public IConnection<T> getComplementaryConnection();

    /**
     * Sets the connection from B's point of view.
     */
    public void setComplementaryConnection(IConnection<T> con);

}