package com.diamantino.electromatic.api.recipe;

import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 *  @author DiamantinoOp
 */
public interface IAlloyFurnaceRecipe extends Recipe<WorldlyContainer> {

    /**
     * Return true if this recipe can be smelted using the input stacks. The input stacks are the 9 inventory slots, so an element can be ItemStack.EMPTY.
     *
     * @param input
     * @return
     */
    boolean matches(NonNullList<ItemStack> input);

    /**
     * The items that are needed in this recipe need to be removed from the input inventory.
     */
    boolean useItems(NonNullList<ItemStack> input, RecipeManager recipeManager);

    ItemStack assemble(NonNullList<ItemStack> input, RecipeManager recipeManager);

    NonNullList<Ingredient> getRequiredItems();

    NonNullList<Integer> getRequiredCount();
}