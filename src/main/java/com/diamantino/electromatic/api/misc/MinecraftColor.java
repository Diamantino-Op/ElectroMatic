package com.diamantino.electromatic.api.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum MinecraftColor {

    WHITE(0xEBEBEB, 0xF0F0F0, 0xFFFFFF, 'f'), //
    ORANGE(0xCC7437, 0xEB8844, 0xF7A165, '6'), //
    MAGENTA(0xB041BA, 0xC354CD, 0xDF79E8, '1'), //
    LIGHT_BLUE(0x4D73C4, 0x6689D3, 0xA5BEF2, 'b'), //
    YELLOW(0xBAAD1E, 0xDECF2A, 0xFAE92F, 'w'), //
    LIME(0x3BB830, 0x41CD34, 0x8FF086, 'a'), //
    PINK(0xB0687B, 0xD88198, 0xE8B3C1, 'd'), //
    GRAY(0x2B2B2B, 0x434343, 0x575757, '8'), //
    LIGHT_GRAY(0xFFFFFF, 0xABABAB, 0xFFFFFF, '7'), //
    CYAN(0x195C78, 0x287697, 0x4688A3, '3'), //
    PURPLE(0x591D8F, 0x7B2FBE, 0x9C60D1, '5'), //
    BLUE(0x172173, 0x253192, 0x3443BF, '1'), //
    BROWN(0x2E1B0F, 0x51301A, 0x63381B, '4'), //
    GREEN(0x25330F, 0x3B511A, 0x577826, '2'), //
    RED(0x872926, 0xB3312C, 0xE03E38, 'c'), //
    BLACK(0x040404, 0x1E1B1B, 0x2B2B2B, '0'), //
    NONE(-1, -1, -1, (char) 0), //
    ANY(-1, -1, -1, (char) 0);

    public static MinecraftColor[] VALID_COLORS = { WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE,
            BLUE, BROWN, GREEN, RED, BLACK };
    public static final MinecraftColor[] WIRE_COLORS = { WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE,
            BLUE, BROWN, GREEN, RED, BLACK };

    private int hexDark, hex, hexLight;
    private char chatColorChar;

    private MinecraftColor(int hexDark, int hex, int hexLight, char chatColorChar) {

        this.hexDark = hexDark;
        this.hex = hex;
        this.hexLight = hexLight;
        this.chatColorChar = chatColorChar;
    }

    public int getHexDark() {

        return hexDark;
    }

    public int getHex() {

        return hex;
    }

    public int getHexLight() {

        return hexLight;
    }

    public String getChatColor() {

        return String.valueOf('\u00a7') + chatColorChar;
    }

    public char getChatColorChar() {

        return chatColorChar;
    }

    public boolean matches(MinecraftColor color) {

        if (this == ANY || color == ANY)
            return true;

        return this == color;
    }

    public boolean canConnect(MinecraftColor color) {

        if (this == ANY || color == ANY || this == NONE || color == NONE)
            return true;

        return this == color;
    }


    public static String getName(MinecraftColor color) {

        switch (color) {
            case WHITE:
                return "white";
            case ORANGE:
                return "orange";
            case MAGENTA:
                return "magenta";
            case LIGHT_BLUE:
                return "light_blue";
            case YELLOW:
                return "yellow";
            case LIME:
                return "lime";
            case PINK:
                return "pink";
            case GRAY:
                return "gray";
            case LIGHT_GRAY:
                return "light_gray";
            case CYAN:
                return "cyan";
            case PURPLE:
                return "purple";
            case BLUE:
                return "blue";
            case BROWN:
                return "brown";
            case GREEN:
                return "green";
            case RED:
                return "red";
            case BLACK:
                return "black";
            case NONE:
                return "none";
            case ANY:
                return "any";
            default:
                return "unknown";

        }
    }

    public static Item getDyeFromColor(MinecraftColor color) {

        switch (color) {
            case WHITE:
                return Items.WHITE_DYE;
            case ORANGE:
                return Items.ORANGE_DYE;
            case MAGENTA:
                return Items.MAGENTA_DYE;
            case LIGHT_BLUE:
                return Items.LIGHT_BLUE_DYE;
            case YELLOW:
                return Items.YELLOW_DYE;
            case LIME:
                return Items.LIME_DYE;
            case PINK:
                return Items.PINK_DYE;
            case GRAY:
                return Items.GRAY_DYE;
            case LIGHT_GRAY:
                return Items.LIGHT_GRAY_DYE;
            case CYAN:
                return Items.CYAN_DYE;
            case PURPLE:
                return Items.PURPLE_DYE;
            case BLUE:
                return Items.BLUE_DYE;
            case BROWN:
                return Items.BROWN_DYE;
            case GREEN:
                return Items.GREEN_DYE;
            case RED:
                return Items.RED_DYE;
            case BLACK:
                return Items.BLACK_DYE;
            default:
                return Items.WHITE_DYE;
        }
    }

    public static MinecraftColor getColorFromString(String color) {

        if (color.equalsIgnoreCase("white"))
            return WHITE;
        else if (color.equalsIgnoreCase("orange"))
            return ORANGE;
        else if (color.equalsIgnoreCase("magenta"))
            return MAGENTA;
        else if (color.equalsIgnoreCase("light_blue"))
            return LIGHT_BLUE;
        else if (color.equalsIgnoreCase("yellow"))
            return YELLOW;
        else if (color.equalsIgnoreCase("lime"))
            return LIME;
        else if (color.equalsIgnoreCase("pink"))
            return PINK;
        else if (color.equalsIgnoreCase("gray"))
            return GRAY;
        else if (color.equalsIgnoreCase("light_gray"))
            return LIGHT_GRAY;
        else if (color.equalsIgnoreCase("cyan"))
            return CYAN;
        else if (color.equalsIgnoreCase("purple"))
            return PURPLE;
        else if (color.equalsIgnoreCase("blue"))
            return BLUE;
        else if (color.equalsIgnoreCase("brown"))
            return BROWN;
        else if (color.equalsIgnoreCase("green"))
            return GREEN;
        else if (color.equalsIgnoreCase("red"))
            return RED;
        else if (color.equalsIgnoreCase("black"))
            return BLACK;
        else if (color.equalsIgnoreCase("none"))
            return NONE;
        else if (color.equalsIgnoreCase("any"))
            return ANY;

        return NONE;
    }
    @Override
    public String toString() {

        return getChatColor();
    }

}