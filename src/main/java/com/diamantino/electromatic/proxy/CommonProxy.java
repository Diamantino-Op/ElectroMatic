package com.diamantino.electromatic.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy {

    private FMLCommonSetupEvent event;

    public void setup(FMLCommonSetupEvent event) {

    }

    public void preInitRenderers() {

    }

    public void initRenderers() {

    }


    public Player getPlayer() {

        return null;
    }

    public boolean isSneakingInGui() {

        return false;
    }

    public String getSavePath() {
        //TODO: Possible Crash
        return Minecraft.getInstance().level.getServer().getServerDirectory().getPath();
    }
}