package com.diamantino.electromatic.compat;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public abstract class CompatModule {


    public abstract void init(FMLCommonSetupEvent ev);

    public abstract void postInit(FMLLoadCompleteEvent ev);

    public abstract void registerBlocks();

    public abstract void registerItems();

    @OnlyIn(Dist.CLIENT)
    public abstract void registerRenders();

    public boolean isScrewdriver(ItemStack item) {

        return false;
    }

}