package com.diamantino.electromatic.registration;

import com.diamantino.electromatic.References;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class EMTags {

    //TODO: Add more wires

    //Blocks
    public static final TagKey<Block> copperWiresBlock = createBlockTag("wires/copper_wires");
    //public static final TagKey<Block> silverWiresBlock = createBlockTag("wires/silver_wires");
    //public static final TagKey<Block> goldWiresBlock = createBlockTag("wires/gold_wires");
    //public static final TagKey<Block> superconductorWiresBlock = createBlockTag("wires/superconductor_wires");

    private static TagKey<Block> createBlockTag(String name) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).createTagKey(new ResourceLocation(References.MOD_ID, name));
    }

    //Items
    public static final TagKey<Item> copperWiresItem = bindItem("wires/copper_wires");
    //public static final TagKey<Item> silverWiresItem = bindItem("wires/silver_wires");
    //public static final TagKey<Item> goldWiresItem = bindItem("wires/gold_wires");
    //public static final TagKey<Item> superconductorWiresItem = bindItem("wires/superconductor_wires");

    private static TagKey<Item> createItemTag(String name)
    {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).createTagKey(new ResourceLocation(References.MOD_ID, name));
    }

    private static TagKey<Item> bindItem(String name) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).createTagKey(new ResourceLocation(References.MOD_ID, name));
    }

}
