package com.diamantino.electromatic.api.wire.electricity;

import com.diamantino.electromatic.api.connect.ConnectionType;
import com.diamantino.electromatic.api.connect.IConnectionCache;
import com.diamantino.electromatic.api.misc.MinecraftColor;
import net.minecraft.core.Direction;

public class ElectricityStorage implements IElectricDevice {

    Electricity electricity = new Electricity(0, 0);

    MinecraftColor color = MinecraftColor.NONE;

    Change change = new Change(0, 0, null, null, false);

    @Override
    public boolean canConnect(Direction side, IElectricDevice dev, ConnectionType type) {
        return true;
    }

    @Override
    public IConnectionCache<? extends IElectricDevice> getElectricConnectionCache() {
        return null;
    }

    @Override
    public Electricity getElectricityValue(Direction side) {
        return electricity;
    }

    @Override
    public void setElectricityValue(Direction side, Electricity electricity) {
        this.electricity = electricity;
    }

    @Override
    public void onElectricityUpdate() {

    }

    @Override
    public Change getChange() {
        return change;
    }

    @Override
    public void setChange(Change changed) {
        change = changed;
    }

    @Override
    public boolean isNormalFace(Direction side) {
        return false;
    }

    @Override
    public MinecraftColor getInsulationColor(Direction side) {
        return color;
    }

    @Override
    public void setInsulationColor(MinecraftColor color) {
        this.color = color;
    }
}