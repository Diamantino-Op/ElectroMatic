package com.diamantino.electromatic.api.wire.electricity;

public enum ElectricWireType {

    COPPER(0.0000000168F, 0x1B1B51, "ingotCopper"),
    SILVER(0.0000000159F, 0x470000, "ingotSilver"),
    GOLD(0.0000000244F, 0x3A003E, "ingotGold"),
    SUPERCONDUCTOR(0F, 0x3A003E, "ingotSuperconductingAlloy");

    private float resistance;
    private int color;
    private String ingotOredictName;

    private ElectricWireType(float resistance, int color, String ingotOredictName) {

        this.resistance = resistance;
        this.color = color;
        this.ingotOredictName = ingotOredictName;
    }

    public float getResistance() {

        return resistance;
    }

    public String getName() {

        return name().toLowerCase().replace("_", "");
    }

    public int getColor() {

        return color;
    }

    public static ElectricWireType getTypeFromName(String name) {

        for (ElectricWireType type : values())
            if (type.getName().replace("_", "").equalsIgnoreCase(name))
                return type;

        return null;
    }

    public String getIngotOredictName() {

        return ingotOredictName;
    }

    public boolean canConnectTo(ElectricWireType type) {

        if (type == null)
            return false;

        return type == this;
    }
}