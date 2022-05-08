package com.diamantino.electromatic.helper;

import com.diamantino.electromatic.utils.ItemStackUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemStackHelper {

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {

        return itemStack1.isEmpty() && itemStack2.isEmpty() || !(itemStack1.isEmpty() || itemStack2.isEmpty())
                && itemStack1.getItem() == itemStack2.getItem() && itemStack1.getDamageValue() == itemStack2.getDamageValue()
                && !(itemStack1.getTag() == null && itemStack2.getTag() != null)
                && (itemStack1.getTag() == null || itemStack1.getTag().equals(itemStack2.getTag()));
    }

    /**
     * Mode is a WidgetFuzzySetting mode.
     *
     * @param stack1
     * @param stack2
     * @param mode
     * @return
     */
    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2, int mode) {
        if (stack1.isEmpty() && !stack2.isEmpty())
            return false;
        if (!stack1.isEmpty() && stack2.isEmpty())
            return false;
        if (stack1.isEmpty() && stack2.isEmpty())
            return true;

        if (mode == 0) {
            //TODO: Make sure this is right)
            return ForgeRegistries.ITEMS.tags().stream().anyMatch(tag -> tag.contains(stack1.getItem()) && tag.contains(stack2.getItem()));
        } else if (mode == 1) {
            return ItemStackUtils.isItemFuzzyEqual(stack1, stack2);
        } else {
            return ForgeRegistries.ITEMS.tags().stream().anyMatch(tag -> tag.contains(stack1.getItem()) && tag.contains(stack2.getItem())) && ItemStack.tagMatches(stack1, stack2);
        }
    }

    public static boolean canStack(ItemStack stack1, ItemStack stack2) {
        return stack1 == ItemStack.EMPTY || stack2 == ItemStack.EMPTY ||
                (stack1.getItem() == stack2.getItem() &&
                        (stack2.getDamageValue() == stack1.getDamageValue()) &&
                        ItemStack.tagMatches(stack2, stack1)) &&
                        stack1.isStackable();
    }

}