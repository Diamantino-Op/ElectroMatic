package com.diamantino.electromatic.proxy;

import com.diamantino.electromatic.client.render.Renderers;
import com.diamantino.electromatic.compat.CompatibilityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    //TODO: Fix

    @Override
    public void setup(FMLCommonSetupEvent event) {
        //DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EMMenuType::registerScreenFactories);
    }

    @Override
    public void preInitRenderers() {
        //MinecraftForge.EVENT_BUS.register(new RenderDebugScreen());
        CompatibilityUtils.registerRenders();

        FMLJavaModLoadingContext.get().getModEventBus().register(new Renderers());
    }


    @Override
    public void initRenderers() {
        Renderers.init();
    }

    @Override
    public Player getPlayer() {

        return Minecraft.getInstance().player;
    }

    @Override
    public boolean isSneakingInGui() {
        return Minecraft.getInstance().options.keyShift.isDown();
    }

    public static Screen getOpenedGui() {
        return Minecraft.getInstance().screen;
    }

    @Override
    public String getSavePath() {

        return Minecraft.getInstance().gameDirectory.getAbsolutePath();
    }
}