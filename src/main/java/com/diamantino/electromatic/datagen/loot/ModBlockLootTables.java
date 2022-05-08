package com.diamantino.electromatic.datagen.loot;

import com.diamantino.electromatic.registration.EMBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        for (Block block : EMBlocks.blockList.stream().map(RegistryObject::get).toArray(Block[]::new)) {
            this.dropSelf(block);
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return EMBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
