package com.diamantino.electromatic.api.pipe;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

/**
 * This interface is implemented by the Pneumatic Tube's logic. you can get the tube from a block by doing
 * @author DiamantinoOp
 */
public interface IVacuumPipe {

    /**
     * The colors that a tube can have. These are in the same order as dye. Also note the 'NONE' as last entry.
     */
    public static enum PipeColor {
        BLACK, RED, GREEN, BROWN, BLUE, PURPLE, CYAN, SILVER, GRAY, PINK, LIME, YELLOW, LIGHT_BLUE, MAGENTA, ORANGE, WHITE, NONE
    }

    /**
     * Returns true if the network accepted the stack.
     * @param stack
     * @param from
     * @param itemColor
     * @param simulate
     * @return
     */
    public boolean injectStack(ItemStack stack, Direction from, PipeColor itemColor, boolean simulate);
}