package com.diamantino.electromatic.client.render;

import net.minecraft.world.item.ItemStack;

/**
 * @author DiamantinoOp
 */
public interface IEMColoredItem {

    int getColor(ItemStack stack, int tintIndex);

}