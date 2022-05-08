package com.diamantino.electromatic.api.power;

import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author DiamantinoOp
 */
public class ElectricityFEStorage extends ElectricityStorage implements IEnergyStorage {


    public ElectricityFEStorage(double max) {
        super(max / 10, 230);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min((int)this.maxEnergy * 10 - (int)energy * 10, Math.min((int)this.maxEnergy * 10, maxReceive));
        if (!simulate)
            energy += energyReceived / 10f;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min((int)energy * 10, Math.min((int)this.maxEnergy * 10, maxExtract));
        if (!simulate)
            energy -= energyExtracted / 10f;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return (int)energy * 10;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int)this.maxEnergy * 10;
    }

    @Override
    public boolean canExtract() {
        return (int)energy > 0;
    }

    @Override
    public boolean canReceive() {
        return (int)energy < (int)maxEnergy;
    }
}