package com.diamantino.electromatic.client.render;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EMItemColor implements ItemColor {

    @Override
    public int getColor(ItemStack itemStack, int renderPass) {
        return ((IEMColoredItem)itemStack.getItem()).getColor(itemStack, renderPass);
    }
}