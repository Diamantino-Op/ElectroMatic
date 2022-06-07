package com.diamantino.electromatic;

import com.diamantino.electromatic.api.*;
import com.diamantino.electromatic.api.chemical.gas.Gas;
import com.diamantino.electromatic.api.chemical.gas.IGasHandler;
import com.diamantino.electromatic.api.chemical.gas.attribute.GasAttributes;
import com.diamantino.electromatic.api.chemical.infuse.IInfusionHandler;
import com.diamantino.electromatic.api.chemical.infuse.InfuseType;
import com.diamantino.electromatic.api.chemical.pigment.IPigmentHandler;
import com.diamantino.electromatic.api.chemical.pigment.Pigment;
import com.diamantino.electromatic.api.chemical.slurry.ISlurryHandler;
import com.diamantino.electromatic.api.chemical.slurry.Slurry;
import com.diamantino.electromatic.api.energy.IStrictEnergyHandler;
import com.diamantino.electromatic.api.gear.ModuleData;
import com.diamantino.electromatic.api.heat.IHeatHandler;
import com.diamantino.electromatic.api.lasers.ILaserDissipation;
import com.diamantino.electromatic.api.lasers.ILaserReceptor;
import com.diamantino.electromatic.api.providers.IItemProvider;
import com.diamantino.electromatic.api.radiation.capability.IRadiationEntity;
import com.diamantino.electromatic.api.radiation.capability.IRadiationShielding;
import com.diamantino.electromatic.api.security.IOwnerObject;
import com.diamantino.electromatic.api.security.ISecurityObject;
import com.diamantino.electromatic.base.*;
import com.diamantino.electromatic.command.CommandMek;
import com.diamantino.electromatic.command.builders.BuildCommand;
import com.diamantino.electromatic.command.builders.Builders;
import com.diamantino.electromatic.config.MekanismConfig;
import com.diamantino.electromatic.config.MekanismModConfig;
import com.diamantino.electromatic.content.boiler.BoilerMultiblockData;
import com.diamantino.electromatic.content.boiler.BoilerValidator;
import com.diamantino.electromatic.content.evaporation.EvaporationMultiblockData;
import com.diamantino.electromatic.content.evaporation.EvaporationValidator;
import com.diamantino.electromatic.content.fission.FissionReactorCache;
import com.diamantino.electromatic.content.fission.FissionReactorMultiblockData;
import com.diamantino.electromatic.content.fission.FissionReactorValidator;
import com.diamantino.electromatic.content.fusion.FusionReactorCache;
import com.diamantino.electromatic.content.fusion.FusionReactorMultiblockData;
import com.diamantino.electromatic.content.fusion.FusionReactorValidator;
import com.diamantino.electromatic.content.gear.MekaSuitDispenseBehavior;
import com.diamantino.electromatic.content.gear.ModuleDispenseBehavior;
import com.diamantino.electromatic.content.gear.ModuleHelper;
import com.diamantino.electromatic.content.quantummatrix.QuantumMatrixMultiblockData;
import com.diamantino.electromatic.content.quantummatrix.QuantumMatrixValidator;
import com.diamantino.electromatic.content.network.BoxedChemicalNetwork;
import com.diamantino.electromatic.content.network.EnergyNetwork;
import com.diamantino.electromatic.content.network.FluidNetwork;
import com.diamantino.electromatic.content.sps.SPSCache;
import com.diamantino.electromatic.content.sps.SPSMultiblockData;
import com.diamantino.electromatic.content.sps.SPSValidator;
import com.diamantino.electromatic.content.tank.TankCache;
import com.diamantino.electromatic.content.tank.TankMultiblockData;
import com.diamantino.electromatic.content.tank.TankValidator;
import com.diamantino.electromatic.content.transporter.PathfinderCache;
import com.diamantino.electromatic.content.transporter.TransporterManager;
import com.diamantino.electromatic.content.turbine.TurbineCache;
import com.diamantino.electromatic.content.turbine.TurbineMultiblockData;
import com.diamantino.electromatic.content.turbine.TurbineValidator;
import com.diamantino.electromatic.integration.MekanismHooks;
import com.diamantino.electromatic.integration.crafttweaker.content.CrTContentUtils;
import com.diamantino.electromatic.item.block.machine.ItemBlockFluidTank;
import com.diamantino.electromatic.lib.MekAnnotationScanner;
import com.diamantino.electromatic.lib.Version;
import com.diamantino.electromatic.lib.frequency.FrequencyManager;
import com.diamantino.electromatic.lib.frequency.FrequencyType;
import com.diamantino.electromatic.lib.multiblock.MultiblockCache;
import com.diamantino.electromatic.lib.multiblock.MultiblockManager;
import com.diamantino.electromatic.lib.radiation.RadiationManager;
import com.diamantino.electromatic.lib.transmitter.TransmitterNetworkRegistry;
import com.diamantino.electromatic.network.PacketHandler;
import com.diamantino.electromatic.network.to_client.PacketTransmitterUpdate;
import com.diamantino.electromatic.recipe.MekanismRecipeType;
import com.diamantino.electromatic.recipe.bin.BinInsertRecipe;
import com.diamantino.electromatic.recipe.condition.ModVersionLoadedCondition;
import com.diamantino.electromatic.registries.*;
import com.diamantino.electromatic.tags.MekanismTags;
import com.diamantino.electromatic.tile.component.TileComponentChunkLoader;
import com.diamantino.electromatic.tile.machine.TileEntityOredictionificator;
import com.diamantino.electromatic.world.GenHandler;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.event.*;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Mod(References.MOD_ID)
public class ElectroMatic
{
    public static final String MODID = References.MOD_ID;
    public static final String MOD_NAME = References.MOD_NAME;
    public static final String LOG_TAG = '[' + MOD_NAME + ']';
    public static final PlayerState playerState = new PlayerState();
    /**
     * ElectroMatic Packet Pipeline
     */
    private final PacketHandler packetHandler;
    /**
     * ElectroMatic logger instance
     */
    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    /**
     * ElectroMatic mod instance
     */
    public static ElectroMatic instance;
    /**
     * ElectroMatic hooks instance
     */
    public static final MekanismHooks hooks = new MekanismHooks();
    /**
     * ElectroMatic version number
     */
    public final Version versionNumber;
    /**
     * ElectroMatic for various structures
     */
    public static final MultiblockManager<TankMultiblockData> tankManager = new MultiblockManager<>("dynamicTank", TankCache::new, TankValidator::new);
    public static final MultiblockManager<QuantumMatrixMultiblockData> quantumMatrixManager = new MultiblockManager<>("quantumMatrix", MultiblockCache::new, QuantumMatrixValidator::new);
    public static final MultiblockManager<BoilerMultiblockData> boilerManager = new MultiblockManager<>("thermoelectricBoiler", MultiblockCache::new, BoilerValidator::new);
    public static final MultiblockManager<EvaporationMultiblockData> evaporationManager = new MultiblockManager<>("evaporation", MultiblockCache::new, EvaporationValidator::new);
    public static final MultiblockManager<SPSMultiblockData> spsManager = new MultiblockManager<>("sps", SPSCache::new, SPSValidator::new);
    public static final MultiblockManager<TurbineMultiblockData> turbineManager = new MultiblockManager<>("industrialTurbine", TurbineCache::new, TurbineValidator::new);
    public static final MultiblockManager<FissionReactorMultiblockData> fissionReactorManager = new MultiblockManager<>("fissionReactor", FissionReactorCache::new, FissionReactorValidator::new);
    public static final MultiblockManager<FusionReactorMultiblockData> fusionReactorManager = new MultiblockManager<>("fusionReactor", FusionReactorCache::new, FusionReactorValidator::new);
    /**
     * List of ElectroMatic modules loaded
     */
    public static final List<IModModule> modulesLoaded = new ArrayList<>();
    /**
     * The server's world tick handler.
     */
    public static final CommonWorldTickHandler worldTickHandler = new CommonWorldTickHandler();
    /**
     * The GameProfile used by the dummy ElectroMatic player
     */
    public static final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes("electromatic".getBytes(StandardCharsets.UTF_8)), LOG_TAG);
    public static final KeySync keyMap = new KeySync();

    private ReloadListener recipeCacheManager;

    public ElectroMatic() {
        instance = this;
        MekanismConfig.registerConfigs(ModLoadingContext.get());

        MinecraftForge.EVENT_BUS.addListener(this::onEnergyTransferred);
        MinecraftForge.EVENT_BUS.addListener(this::onChemicalTransferred);
        MinecraftForge.EVENT_BUS.addListener(this::onLiquidTransferred);
        MinecraftForge.EVENT_BUS.addListener(this::onWorldLoad);
        MinecraftForge.EVENT_BUS.addListener(this::onWorldUnload);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopped);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::addReloadListenersLowest);
        MinecraftForge.EVENT_BUS.addListener(BinInsertRecipe::onCrafting);
        MinecraftForge.EVENT_BUS.addListener(this::onTagsReload);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, GenHandler::onBiomeLoad);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::onConfigLoad);
        modEventBus.addListener(this::imcQueue);
        modEventBus.addListener(this::imcHandle);
        MekanismItems.ITEMS.register(modEventBus);
        MekanismBlocks.BLOCKS.register(modEventBus);
        MekanismFluids.FLUIDS.register(modEventBus);
        MekanismContainerTypes.CONTAINER_TYPES.register(modEventBus);
        MekanismEntityTypes.ENTITY_TYPES.register(modEventBus);
        MekanismTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        MekanismSounds.SOUND_EVENTS.register(modEventBus);
        MekanismParticleTypes.PARTICLE_TYPES.register(modEventBus);
        MekanismHeightProviderTypes.HEIGHT_PROVIDER_TYPES.register(modEventBus);
        MekanismIntProviderTypes.INT_PROVIDER_TYPES.register(modEventBus);
        MekanismPlacementModifiers.PLACEMENT_MODIFIERS.register(modEventBus);
        MekanismFeatures.FEATURES.register(modEventBus);
        MekanismFeatures.SETUP_FEATURES.register(modEventBus);
        MekanismRecipeType.RECIPE_TYPES.register(modEventBus);
        MekanismRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        MekanismDataSerializers.DATA_SERIALIZERS.register(modEventBus);
        MekanismGases.GASES.createAndRegister(modEventBus, Gas.class, builder -> builder.hasTags().setDefaultKey(rl("empty")));
        MekanismInfuseTypes.INFUSE_TYPES.createAndRegister(modEventBus, InfuseType.class, builder -> builder.hasTags().setDefaultKey(rl("empty")));
        MekanismPigments.PIGMENTS.createAndRegister(modEventBus, Pigment.class, builder -> builder.hasTags().setDefaultKey(rl("empty")));
        MekanismSlurries.SLURRIES.createAndRegister(modEventBus, Slurry.class, builder -> builder.hasTags().setDefaultKey(rl("empty")));
        //noinspection rawtypes,unchecked
        MekanismModules.MODULES.createAndRegister(modEventBus, (Class) ModuleData.class);
        modEventBus.addGenericListener(Gas.class, this::registerGases);
        modEventBus.addGenericListener(InfuseType.class, this::registerInfuseTypes);
        modEventBus.addGenericListener(Pigment.class, this::registerPigments);
        modEventBus.addGenericListener(Slurry.class, this::registerSlurries);
        modEventBus.addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);
        //Set our version number to match the mods.toml file, which matches the one in our build.gradle
        versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
        packetHandler = new PacketHandler();
        //Super early hooks, only reliable thing is for checking dependencies that we declare we are after
        hooks.hookConstructor(modEventBus);
        if (hooks.CraftTweakerLoaded && !DatagenModLoader.isRunningDataGen()) {
            //Attempt to grab the mod event bus for CraftTweaker so that we can register our custom content in their namespace
            // to make it clearer which chemicals were added by CraftTweaker, and which are added by actual mods.
            // Gracefully fallback to our event bus if something goes wrong with getting CrT's and just then have the log have
            // warnings about us registering things in their namespace.
            IEventBus crtModEventBus = modEventBus;
            Optional<? extends ModContainer> crtModContainer = ModList.get().getModContainerById(MekanismHooks.CRAFTTWEAKER_MOD_ID);
            if (crtModContainer.isPresent()) {
                ModContainer container = crtModContainer.get();
                if (container instanceof FMLModContainer modContainer) {
                    crtModEventBus = modContainer.getEventBus();
                }
            }
            //Register these at lowest priority to try and ensure they get later ids in the chemical registries
            crtModEventBus.addGenericListener(Gas.class, EventPriority.LOWEST, CrTContentUtils::registerCrTGases);
            crtModEventBus.addGenericListener(InfuseType.class, EventPriority.LOWEST, CrTContentUtils::registerCrTInfuseTypes);
            crtModEventBus.addGenericListener(Pigment.class, EventPriority.LOWEST, CrTContentUtils::registerCrTPigments);
            crtModEventBus.addGenericListener(Slurry.class, EventPriority.LOWEST, CrTContentUtils::registerCrTSlurries);
        }
    }

    public static PacketHandler packetHandler() {
        return instance.packetHandler;
    }

    //Register the empty chemicals
    private void registerGases(RegistryEvent.Register<Gas> event) {
        ElectroMaticAPI.EMPTY_GAS.setRegistryName(rl("empty"));
        event.getRegistry().register(ElectroMaticAPI.EMPTY_GAS);
    }

    private void registerInfuseTypes(RegistryEvent.Register<InfuseType> event) {
        ElectroMaticAPI.EMPTY_INFUSE_TYPE.setRegistryName(rl("empty"));
        event.getRegistry().register(ElectroMaticAPI.EMPTY_INFUSE_TYPE);
    }

    private void registerPigments(RegistryEvent.Register<Pigment> event) {
        ElectroMaticAPI.EMPTY_PIGMENT.setRegistryName(rl("empty"));
        event.getRegistry().register(ElectroMaticAPI.EMPTY_PIGMENT);
    }

    private void registerSlurries(RegistryEvent.Register<Slurry> event) {
        ElectroMaticAPI.EMPTY_SLURRY.setRegistryName(rl("empty"));
        event.getRegistry().register(ElectroMaticAPI.EMPTY_SLURRY);
    }

    private void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        CraftingHelper.register(ModVersionLoadedCondition.Serializer.INSTANCE);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ElectroMatic.MODID, path);
    }

    private void setRecipeCacheManager(ReloadListener manager) {
        if (recipeCacheManager == null) {
            recipeCacheManager = manager;
        } else {
            logger.warn("Recipe cache manager has already been set.");
        }
    }

    public ReloadListener getRecipeCacheManager() {
        return recipeCacheManager;
    }

    private void onTagsReload(TagsUpdatedEvent event) {
        TagCache.resetTagCaches();
    }

    private void addReloadListenersLowest(AddReloadListenerEvent event) {
        //Note: We register reload listeners here which we want to make sure run after CraftTweaker or any other mods that may modify recipes or loot tables
        event.addListener(getRecipeCacheManager());
    }

    private void registerCommands(RegisterCommandsEvent event) {
        BuildCommand.register("boiler", MekanismLang.BOILER, new Builders.BoilerBuilder());
        BuildCommand.register("matrix", MekanismLang.MATRIX, new Builders.MatrixBuilder());
        BuildCommand.register("tank", MekanismLang.DYNAMIC_TANK, new Builders.TankBuilder());
        BuildCommand.register("evaporation", MekanismLang.EVAPORATION_PLANT, new Builders.EvaporationBuilder());
        BuildCommand.register("sps", MekanismLang.SPS, new Builders.SPSBuilder());
        event.getDispatcher().register(CommandMek.register());
    }

    private void serverStopped(ServerStoppedEvent event) {
        //Clear all cache data, wait until server stopper though so that we make sure saving can use any data it needs
        playerState.clear(false);
        worldTickHandler.resetChunkData();
        FrequencyType.clear();
        BoilerMultiblockData.hotMap.clear();

        //Reset consistent managers
        RadiationManager.INSTANCE.reset();
        MultiblockManager.reset();
        FrequencyManager.reset();
        TransporterManager.reset();
        PathfinderCache.reset();
        TransmitterNetworkRegistry.reset();
    }

    private void imcQueue(InterModEnqueueEvent event) {
        //IMC messages we send to other mods
        hooks.sendIMCMessages(event);
        //IMC messages that we are sending to ourselves
        ElectroMaticIMC.addModulesToAll(MekanismModules.ENERGY_UNIT);
        ElectroMaticIMC.addMekaSuitModules(MekanismModules.LASER_DISSIPATION_UNIT, MekanismModules.RADIATION_SHIELDING_UNIT);
        ElectroMaticIMC.addMekaToolModules(MekanismModules.ATTACK_AMPLIFICATION_UNIT, MekanismModules.SILK_TOUCH_UNIT, MekanismModules.VEIN_MINING_UNIT,
                MekanismModules.FARMING_UNIT, MekanismModules.SHEARING_UNIT, MekanismModules.TELEPORTATION_UNIT, MekanismModules.EXCAVATION_ESCALATION_UNIT);
        ElectroMaticIMC.addMekaSuitHelmetModules(MekanismModules.ELECTROLYTIC_BREATHING_UNIT, MekanismModules.INHALATION_PURIFICATION_UNIT,
                MekanismModules.VISION_ENHANCEMENT_UNIT, MekanismModules.NUTRITIONAL_INJECTION_UNIT);
        ElectroMaticIMC.addMekaSuitBodyarmorModules(MekanismModules.JETPACK_UNIT, MekanismModules.GRAVITATIONAL_MODULATING_UNIT, MekanismModules.CHARGE_DISTRIBUTION_UNIT,
                MekanismModules.DOSIMETER_UNIT, MekanismModules.GEIGER_UNIT, MekanismModules.ELYTRA_UNIT);
        ElectroMaticIMC.addMekaSuitPantsModules(MekanismModules.LOCOMOTIVE_BOOSTING_UNIT, MekanismModules.GYROSCOPIC_STABILIZATION_UNIT);
        ElectroMaticIMC.addMekaSuitBootsModules(MekanismModules.HYDRAULIC_PROPULSION_UNIT, MekanismModules.MAGNETIC_ATTRACTION_UNIT, MekanismModules.FROST_WALKER_UNIT);
        ElectroMaticIMC.addMekaSuitHelmetModules(MekanismModules.SOLAR_RECHARGING_UNIT);
        ElectroMaticIMC.addMekaSuitPantsModules(MekanismModules.GEOTHERMAL_GENERATOR_UNIT);
    }

    private void imcHandle(InterModProcessEvent event) {
        ModuleHelper.INSTANCE.processIMC();
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IGasHandler.class);
        event.register(IInfusionHandler.class);
        event.register(IPigmentHandler.class);
        event.register(ISlurryHandler.class);
        event.register(IHeatHandler.class);
        event.register(IStrictEnergyHandler.class);

        event.register(IConfigurable.class);
        event.register(IAlloyInteraction.class);
        event.register(IConfigCardAccess.class);
        event.register(IEvaporationSolar.class);
        event.register(ILaserReceptor.class);
        event.register(ILaserDissipation.class);

        event.register(IRadiationShielding.class);
        event.register(IRadiationEntity.class);

        event.register(IOwnerObject.class);
        event.register(ISecurityObject.class);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        //Initialization notification
        logger.info("Version {} initializing...", versionNumber);
        hooks.hookCommonSetup();
        setRecipeCacheManager(new ReloadListener());

        //1mB hydrogen + 2*bioFuel/tick*200ticks/100mB * 20x efficiency bonus
        MekanismGases.ETHENE.get().addAttribute(new GasAttributes.Fuel(MekanismConfig.general.ETHENE_BURN_TIME,
                () -> MekanismConfig.general.FROM_H2.get().add(MekanismConfig.generators.bioGeneration.get()
                        .multiply(2L * MekanismConfig.general.ETHENE_BURN_TIME.get()))));

        event.enqueueWork(() -> {
            //Ensure our tags are all initialized
            MekanismTags.init();
            //Collect annotation scan data
            MekAnnotationScanner.collectScanData();
            //Add chunk loading callbacks
            ForgeChunkManager.setForcedChunkLoadingCallback(MODID, TileComponentChunkLoader.ChunkValidationCallback.INSTANCE);
            //Register dispenser behaviors
            MekanismFluids.FLUIDS.registerBucketDispenserBehavior();
            registerFluidTankBehaviors(MekanismBlocks.BASIC_FLUID_TANK, MekanismBlocks.ADVANCED_FLUID_TANK, MekanismBlocks.ELITE_FLUID_TANK,
                    MekanismBlocks.ULTIMATE_FLUID_TANK, MekanismBlocks.CREATIVE_FLUID_TANK);
            registerDispenseBehavior(new ModuleDispenseBehavior(), MekanismItems.MEKA_TOOL);
            registerDispenseBehavior(new MekaSuitDispenseBehavior(), MekanismItems.MEKASUIT_HELMET, MekanismItems.MEKASUIT_BODYARMOR, MekanismItems.MEKASUIT_PANTS,
                    MekanismItems.MEKASUIT_BOOTS);
            BuildCommand.register("turbine", MekanismLang.TURBINE, new MekanismBuilders.TurbineBuilder());
            BuildCommand.register("fission", MekanismLang.FISSION_REACTOR, new MekanismBuilders.FissionReactorBuilder());
            BuildCommand.register("fusion", MekanismLang.FUSION_REACTOR, new MekanismBuilders.FusionReactorBuilder());
        });

        //Register player tracker
        MinecraftForge.EVENT_BUS.register(new CommonPlayerTracker());
        MinecraftForge.EVENT_BUS.register(new CommonPlayerTickHandler());
        MinecraftForge.EVENT_BUS.register(worldTickHandler);

        MinecraftForge.EVENT_BUS.register(RadiationManager.INSTANCE);

        //Register with TransmitterNetworkRegistry
        TransmitterNetworkRegistry.initiate();

        //Packet registrations
        packetHandler.initialize();

        //Fake player info
        logger.info("Fake player readout: UUID = {}, name = {}", gameProfile.getId(), gameProfile.getName());
        logger.info("Mod loaded.");
    }

    private static void registerDispenseBehavior(DispenseItemBehavior behavior, IItemProvider... itemProviders) {
        for (IItemProvider itemProvider : itemProviders) {
            DispenserBlock.registerBehavior(itemProvider.asItem(), behavior);
        }
    }

    private static void registerFluidTankBehaviors(IItemProvider... itemProviders) {
        registerDispenseBehavior(ItemBlockFluidTank.FluidTankItemDispenseBehavior.INSTANCE);
        for (IItemProvider itemProvider : itemProviders) {
            Item item = itemProvider.asItem();
            CauldronInteraction.EMPTY.put(item, ItemBlockFluidTank.BasicCauldronInteraction.EMPTY);
            CauldronInteraction.WATER.put(item, ItemBlockFluidTank.BasicDrainCauldronInteraction.WATER);
            CauldronInteraction.LAVA.put(item, ItemBlockFluidTank.BasicDrainCauldronInteraction.LAVA);
        }
    }

    private void onEnergyTransferred(EnergyNetwork.EnergyTransferEvent event) {
        packetHandler.sendToReceivers(new PacketTransmitterUpdate(event.network), event.network);
    }

    private void onChemicalTransferred(BoxedChemicalNetwork.ChemicalTransferEvent event) {
        packetHandler.sendToReceivers(new PacketTransmitterUpdate(event.network, event.transferType), event.network);
    }

    private void onLiquidTransferred(FluidNetwork.FluidTransferEvent event) {
        packetHandler.sendToReceivers(new PacketTransmitterUpdate(event.network, event.fluidType), event.network);
    }

    private void onConfigLoad(ModConfigEvent configEvent) {
        //Note: We listen to both the initial load and the reload, to make sure that we fix any accidentally
        // cached values from calls before the initial loading
        ModConfig config = configEvent.getConfig();
        //Make sure it is for the same modid as us
        if (config.getModId().equals(MODID) && config instanceof MekanismModConfig mekConfig) {
            mekConfig.clearCache();
        }
    }

    private void onWorldLoad(WorldEvent.Load event) {
        playerState.init(event.getWorld());
    }

    private void onWorldUnload(WorldEvent.Unload event) {
        // Make sure the global fake player drops its reference to the World
        // when the server shuts down
        if (event.getWorld() instanceof ServerLevel) {
            MekFakePlayer.releaseInstance(event.getWorld());
        }
        if (event.getWorld() instanceof Level level && MekanismConfig.general.validOredictionificatorFilters.hasInvalidationListeners()) {
            //Remove any invalidation listeners that loaded oredictionificators might have added if the OD was in the given level
            MekanismConfig.general.validOredictionificatorFilters.removeInvalidationListenersMatching(listener ->
                    listener instanceof TileEntityOredictionificator.ODConfigValueInvalidationListener odListener && odListener.isIn(level));
        }
    }
}
