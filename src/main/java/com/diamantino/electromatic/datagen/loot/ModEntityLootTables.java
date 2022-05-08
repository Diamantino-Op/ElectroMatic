package com.diamantino.electromatic.datagen.loot;

import net.minecraft.data.loot.EntityLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public class ModEntityLootTables extends EntityLoot {
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> p_124377_) {
        super.accept(p_124377_);
    }
}
