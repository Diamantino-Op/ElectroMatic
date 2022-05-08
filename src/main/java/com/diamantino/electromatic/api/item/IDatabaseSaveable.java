package com.diamantino.electromatic.api.item;

import java.util.List;

import net.minecraft.world.item.ItemStack;

/**
 * Implemented by items/blocks that can be copied and shared in a Circuit Database.
 * When copied, the entire NBTTag will be copied to the outputStack.
 * @author DiamantinoOp
 */
public interface IDatabaseSaveable {

    /**
     * First layer of checking if the item is valid, when the item is being tried to be placed in the copy/share slot, or output slot.
     * @param stack
     * @return
     */
    public boolean canGoInCopySlot(ItemStack stack);

    /**
     * Return true if the ItemStack that's being 'injected' with info is a stack that can be injected.
     * This method is only called when templateStack.sameItem(outputStack) returned true.
     * @param templateStack
     * @param outputStack
     * @return false to disallow copying.
     */
    public boolean canCopy(ItemStack templateStack, ItemStack outputStack);

    /**
     * Items that contain items (an Integrated Circuit with gates on it) need to compare the input and output, and tell
     * which items are required. With this method you can tell the Circuit Database what items the item carries, so it can
     * calculate which items it needs.
     * @param stack
     * @return ItemStack.EMPTY is a valid return.
     */
    public List<ItemStack> getItemsOnStack(ItemStack stack);

}