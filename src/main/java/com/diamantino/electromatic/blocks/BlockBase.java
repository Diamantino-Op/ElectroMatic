package com.diamantino.electromatic.blocks;

import com.diamantino.electromatic.api.misc.MinecraftColor;
import com.diamantino.electromatic.registration.EMBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockBase extends Block {

    private boolean wip = false;

    public BlockBase(Material material) {
        super(Properties.of(material).sound(SoundType.STONE).strength(3.0F));
    }

    public BlockBase(Properties properties) {
        super(properties);
    }

    public boolean getWIP(){
        return wip;
    }

    public BlockBase setWIP(boolean wip) {

        this.wip = wip;
        return this;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        super.appendHoverText(stack, world, tooltip, advanced);
        if(wip){
            tooltip.add(new TextComponent(MinecraftColor.RED.getChatColor() + "WIP") );
        }
    }

}