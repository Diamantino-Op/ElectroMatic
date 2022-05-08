package com.diamantino.electromatic.datagen;

import com.diamantino.electromatic.References;
import com.diamantino.electromatic.api.misc.MinecraftColor;
import com.diamantino.electromatic.blocks.BlockElectricWire;
import com.diamantino.electromatic.registration.EMBlocks;
import com.diamantino.electromatic.registration.EMTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        int i = 0;
        for (BlockElectricWire copper_wire : EMBlocks.blockCopperWireArray.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)) {
            ShapedRecipeBuilder.shaped(copper_wire)
                    .define('C', EMTags.copperWiresItem)
                    .define('D', MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[i]))
                    .pattern("CCC")
                    .pattern("CDC")
                    .pattern("CCC")
                    .unlockedBy("has_" + MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[i]).toString(), inventoryTrigger(ItemPredicate.Builder.item()
                            .of(MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[i])).build()))
                    .save(pFinishedRecipeConsumer, new ResourceLocation(References.MOD_ID, MinecraftColor.getName(MinecraftColor.WIRE_COLORS[i]) + "_copper_wire"));
            i++;
        }

        int j = 0;
        for (BlockElectricWire silver_wire : EMBlocks.blockSilverWireArray.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)) {
            ShapedRecipeBuilder.shaped(silver_wire)
                    .define('C', EMTags.silverWiresItem)
                    .define('D', MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[j]))
                    .pattern("CCC")
                    .pattern("CDC")
                    .pattern("CCC")
                    .unlockedBy("has_" + MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[j]).toString(), inventoryTrigger(ItemPredicate.Builder.item()
                            .of(MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[j])).build()))
                    .save(pFinishedRecipeConsumer, new ResourceLocation(References.MOD_ID, MinecraftColor.getName(MinecraftColor.WIRE_COLORS[j]) + "_silver_wire"));
            j++;
        }

        int k = 0;
        for (BlockElectricWire gold_wire : EMBlocks.blockGoldWireArray.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)) {
            ShapedRecipeBuilder.shaped(gold_wire)
                    .define('C', EMTags.goldWiresItem)
                    .define('D', MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[k]))
                    .pattern("CCC")
                    .pattern("CDC")
                    .pattern("CCC")
                    .unlockedBy("has_" + MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[k]).toString(), inventoryTrigger(ItemPredicate.Builder.item()
                            .of(MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[k])).build()))
                    .save(pFinishedRecipeConsumer, new ResourceLocation(References.MOD_ID, MinecraftColor.getName(MinecraftColor.WIRE_COLORS[k]) + "_gold_wire"));
            k++;
        }

        int l = 0;
        for (BlockElectricWire superconductor_wire : EMBlocks.blockSuperconductorWireArray.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)) {
            ShapedRecipeBuilder.shaped(superconductor_wire)
                    .define('C', EMTags.superconductorWiresItem)
                    .define('D', MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[l]))
                    .pattern("CCC")
                    .pattern("CDC")
                    .pattern("CCC")
                    .unlockedBy("has_" + MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[l]).toString(), inventoryTrigger(ItemPredicate.Builder.item()
                            .of(MinecraftColor.getDyeFromColor(MinecraftColor.WIRE_COLORS[l])).build()))
                    .save(pFinishedRecipeConsumer, new ResourceLocation(References.MOD_ID, MinecraftColor.getName(MinecraftColor.WIRE_COLORS[l]) + "_superconductor_wire"));
            l++;
        }
    }

    @Override
    public String getName() {
        return "Electromatic Recipes";
    }
}
