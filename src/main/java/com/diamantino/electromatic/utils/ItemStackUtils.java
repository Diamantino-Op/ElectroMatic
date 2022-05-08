package com.diamantino.electromatic.utils;

import com.diamantino.electromatic.compat.CompatibilityUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 *
 * @author DiamantinoOp
 */

public class ItemStackUtils {

    public static boolean isItemFuzzyEqual(ItemStack stack1, ItemStack stack2) {

        if (isSameOreDictStack(stack1, stack2)) {
            return true;
        }
        if (stack1.getItem() != stack2.getItem())
            return false;
        return stack1.equals(stack2);
    }

    public static boolean isSameOreDictStack(ItemStack stack1, ItemStack stack2) {
        return ForgeRegistries.ITEMS.tags().stream().anyMatch(tag -> tag.contains(stack1.getItem()) && tag.contains(stack2.getItem()));
    }

    public static boolean isScrewdriver(ItemStack item) {

        if (item.isEmpty())
            return false;
        if (item.getItem() == null)
            return false;

        return CompatibilityUtils.isScrewdriver(item);
    }
}