package com.diamantino.electromatic.api.wire.electricity;

import net.minecraft.core.Direction;

public class Change {
    public float volatageChange;
    public float amperageChange;
    public Direction changeSide;
    public ChangeType type;
    public boolean hasChanged;

    public Change(float volatageChange, float amperageChange, Direction changeSide, ChangeType type, boolean hasChanged) {
        this.volatageChange = volatageChange;
        this.amperageChange = amperageChange;
        this.changeSide = changeSide;
        this.type = type;
        this.hasChanged = hasChanged;
    }

    public void resetChange() {
        this.volatageChange = 0;
        this.amperageChange = 0;
        changeSide = null;
        type = null;
        hasChanged = false;
    }

    public enum ChangeType {
        ADDED, REMOVED
    }
}