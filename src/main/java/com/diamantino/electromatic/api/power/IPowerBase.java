package com.diamantino.electromatic.api.power;

/**
 * @author DiamantinoOp
 */
public interface IPowerBase {

    double getEnergy();
    double getMaxEnergy();

    double getVoltage();
    double getMaxVoltage();
    double getCurrent();

    /**
     * Negative energy for removal
     * @param energy
     * @param simulate when true, no power will be added, but the return value can be used to determine if adding or consuming power is possible.
     * @return the added power.
     */
    double addEnergy(double energy, boolean simulate);

}