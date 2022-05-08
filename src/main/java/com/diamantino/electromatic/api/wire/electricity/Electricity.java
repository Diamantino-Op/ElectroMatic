package com.diamantino.electromatic.api.wire.electricity;

public class Electricity {
    private float voltage;
    private float amperage;

    public Electricity (float voltage, float amperage) {
        this.voltage = voltage;
        this.amperage = amperage;
    }

    public float getVoltage () {
        return this.voltage;
    }

    public float getAmperage () {
        return this.amperage;
    }

    public void setVoltage (float voltage) {
        this.voltage = voltage;
    }

    public void setAmperage (float amperage) {
        this.amperage = amperage;
    }

    public float calculateWattage (float voltage, float amperage) {
        return voltage * amperage;
    }
}

