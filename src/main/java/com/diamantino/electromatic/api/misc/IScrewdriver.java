package com.diamantino.electromatic.api.misc;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IScrewdriver {

    public boolean damage(ItemStack stack, int damage, Player player, boolean simulated);

}