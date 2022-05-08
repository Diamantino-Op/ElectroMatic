package com.diamantino.electromatic;

import com.diamantino.electromatic.api.EMApi;
import com.diamantino.electromatic.api.wire.electricity.CapabilityElectricDevice;
import com.diamantino.electromatic.compat.CompatibilityUtils;
import com.diamantino.electromatic.events.EMEventHandler;
import com.diamantino.electromatic.proxy.ClientProxy;
import com.diamantino.electromatic.proxy.CommonProxy;
import com.diamantino.electromatic.registration.EMBlockEntityTypes;
import com.diamantino.electromatic.registration.EMBlocks;
import com.diamantino.electromatic.registration.EMItems;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod(References.MOD_ID)
public class ElectroMatic
{
    //TODO: Fix

    public static ElectroMatic instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public ElectroMatic(){
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BPConfig.spec);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::complete);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerCapabilities);

        EMItems.register(bus);
        EMBlocks.register(bus);
        EMBlockEntityTypes.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
        //MinecraftForge.EVENT_BUS.register(BPEnchantments.class);

        EMEventHandler eventHandler = new EMEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);

        EMApi.init(new ElectroMaticAPI());
        proxy.preInitRenderers();

        /*FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Feature.class, BPWorldGen::registerFeatures);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Feature.class, WorldGenOres::registerOres);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Feature.class, WorldGenFlowers::registerFlowers);

        MinecraftForge.EVENT_BUS.register(new BPWorldGen());
        MinecraftForge.EVENT_BUS.register(new WorldGenOres());
        MinecraftForge.EVENT_BUS.register(new WorldGenFlowers());*/
    }

    public static Logger log = LogManager.getLogger(References.MOD_ID);

    public void setup(FMLCommonSetupEvent event) {
        //event.enqueueWork(EMNetworkHandler::init);
        proxy.setup(event);
        CompatibilityUtils.init(event);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event){
        CapabilityElectricDevice.register(event);
    }

    public void complete(FMLLoadCompleteEvent event) {
        proxy.initRenderers();
        CompatibilityUtils.postInit(event);
        //Recipes.init();
    }

    @SubscribeEvent
    public void onResourceReload(AddReloadListenerEvent event) {
        //Add Reload Listener for the Alloy Furnace Recipe Generator
        //event.addListener(new BPRecyclingReloadListener(event.getServerResources()));
    }

    @SubscribeEvent
    public void onLootLoad(LootTableLoadEvent event){
        ResourceLocation grass = new ResourceLocation("minecraft", "blocks/tall_grass");
        /*if (event.getName().equals(grass)){
            event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation("electromatic", "blocks/tall_grass"))).name("electromatic:tall_grass").build());
        }*/
    }
}
