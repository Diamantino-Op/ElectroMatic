package com.diamantino.electromatic.registration;

import com.diamantino.electromatic.References;
import com.diamantino.electromatic.blocks.BlockElectricWire;
import com.diamantino.electromatic.tiles.TileEMMicroblock;
import com.diamantino.electromatic.tiles.TileEMMultipart;
import com.diamantino.electromatic.tiles.electronics.TileElectricWire;
import com.diamantino.electromatic.tiles.electronics.TilePipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = References.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EMBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, References.MOD_ID);

    public static final RegistryObject<BlockEntityType<TileElectricWire>> wire = BLOCK_ENTITIES.register("wire", () -> BlockEntityType.Builder.of(TileElectricWire::new, EMBlocks.blockWireAll.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)).build(null));
    public static final RegistryObject<BlockEntityType<TileEMMultipart>> multipart = BLOCK_ENTITIES.register("multipart", () -> BlockEntityType.Builder.of(TileEMMultipart::new, EMBlocks.multipart.get()).build(null));
    public static final RegistryObject<BlockEntityType<TileEMMicroblock>> microblock = BLOCK_ENTITIES.register("microblock", () -> BlockEntityType.Builder.of(TileEMMicroblock::new, EMBlocks.microblocks.toArray(new Block[0])).build(null));
    public static final RegistryObject<BlockEntityType<TilePipe>> pipe = BLOCK_ENTITIES.register("pipe", () -> BlockEntityType.Builder.of(TilePipe::new, EMBlocks.pipe.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    /**
     * Method to register the TE's to the game. If a TE is not registered, it _will_ cause issues!
     */
    /*@Mod.EventBusSubscriber(modid = References.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
        @SubscribeEvent
        public static void onBlockEntityTypeRegistry(final RegistryEvent.Register<BlockEntityType<?>> event) {
            event.getRegistry().registerAll(
                    BlockEntityType.Builder.of(TileAlloyFurnace::new, BPBlocks.alloyfurnace).build(null).setRegistryName(ContainerNames.ALLOY_FURNACE),
                    BlockEntityType.Builder.of(TileBlockBreaker::new, BPBlocks.block_breaker).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "block_breaker")),
                    BlockEntityType.Builder.of(TileIgniter::new, BPBlocks.igniter).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "igniter")),
                    BlockEntityType.Builder.of(TileBuffer::new, BPBlocks.buffer).build(null).setRegistryName(ContainerNames.BUFFER),
                    BlockEntityType.Builder.of(TileTransposer::new, BPBlocks.transposer).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "transposer")),
                    BlockEntityType.Builder.of(TileSortingMachine::new, BPBlocks.sorting_machine).build(null).setRegistryName(ContainerNames.SORTING_MACHINE),
                    BlockEntityType.Builder.of(TileTube::new, BPBlocks.tube).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "tube")),
                    //BlockEntityType.Builder.of(TileSortron::new, BPBlocks.sortron).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "sortron")),
                    BlockEntityType.Builder.of(TileEngine::new, BPBlocks.engine).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "engine")),
                    BlockEntityType.Builder.of(TileBattery::new, BPBlocks.battery).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "battery")),
                    BlockEntityType.Builder.of(TileBlulectricCable::new, BPBlocks.blulectric_cable).build(null).setRegistryName(new ResourceLocation(Refs.MODID, Refs.BLULECTRICCABLE_NAME)),
                    BlockEntityType.Builder.of(TileBlulectricFurnace::new, BPBlocks.blulectric_furnace).build(null).setRegistryName(new ResourceLocation(Refs.MODID, Refs.BLULECTRICFURNACE_NAME)),
                    BlockEntityType.Builder.of(TileBlulectricAlloyFurnace::new, BPBlocks.blulectric_alloyfurnace).build(null).setRegistryName(new ResourceLocation(Refs.MODID, Refs.BLULECTRICALLOYFURNACE_NAME)),
                    BlockEntityType.Builder.of(TileThermopile::new, BPBlocks.thermopile).build(null).setRegistryName(new ResourceLocation(Refs.MODID, Refs.THERMOPILE_NAME)),
                    //BlockEntityType.Builder.of(TileWindmill::new, BPBlocks.windmill).build(null).setRegistryName(ContainerNames.WINDMILL),
                    BlockEntityType.Builder.of(TileKineticGenerator::new, BPBlocks.kinetic_generator).build(null).setRegistryName(ContainerNames.KINETIC_GENERATOR),
                    BlockEntityType.Builder.of(TileSolarPanel::new, BPBlocks.solarpanel).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "solar_panel")),
                    BlockEntityType.Builder.of(TileDeployer::new, BPBlocks.deployer).build(null).setRegistryName(ContainerNames.DEPLOYER),
                    BlockEntityType.Builder.of(TileEjector::new, BPBlocks.ejector).build(null).setRegistryName(ContainerNames.EJECTOR),
                    BlockEntityType.Builder.of(TileRelay::new, BPBlocks.relay).build(null).setRegistryName(ContainerNames.RELAY),
                    BlockEntityType.Builder.of(TileFilter::new, BPBlocks.filter).build(null).setRegistryName(ContainerNames.FILTER),
                    BlockEntityType.Builder.of(TileRetriever::new, BPBlocks.retriever).build(null).setRegistryName(ContainerNames.RETRIEVER),
                    BlockEntityType.Builder.of(TileRegulator::new, BPBlocks.regulator).build(null).setRegistryName(ContainerNames.REGULATOR),
                    BlockEntityType.Builder.of(TileItemDetector::new, BPBlocks.item_detector).build(null).setRegistryName(ContainerNames.ITEM_DETECTOR),
                    BlockEntityType.Builder.of(TileManager::new, BPBlocks.manager).build(null).setRegistryName(ContainerNames.MANAGER),
                    BlockEntityType.Builder.of(TileProjectTable::new, BPBlocks.project_table).build(null).setRegistryName(ContainerNames.PROJECT_TABLE),
                    BlockEntityType.Builder.of(TileAutoProjectTable::new, BPBlocks.auto_project_table).build(null).setRegistryName(ContainerNames.AUTO_PROJECT_TABLE),
                    BlockEntityType.Builder.of(TileCircuitTable::new, BPBlocks.circuit_table).build(null).setRegistryName(ContainerNames.CIRCUIT_TABLE),
                    BlockEntityType.Builder.of(TileCircuitDatabase::new, BPBlocks.circuit_database).build(null).setRegistryName(ContainerNames.CIRCUITDATABASE_MAIN),
                    BlockEntityType.Builder.of(TileLamp::new, BPBlocks.allLamps.toArray(new Block[0])).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "lamp")),
                    BlockEntityType.Builder.of(TileElectricWire::new, EMBlocks.blockCopperWireArray.toArray(new Block[0])).build(null).setRegistryName(new ResourceLocation(References.MOD_ID, "wire")),
                    BlockEntityType.Builder.of(TileElectricWire::new, EMBlocks.blockSilverWireArray.toArray(new Block[0])).build(null).setRegistryName(new ResourceLocation(References.MOD_ID, "wire")),
                    BlockEntityType.Builder.of(TileElectricWire::new, EMBlocks.blockGoldWireArray.toArray(new Block[0])).build(null).setRegistryName(new ResourceLocation(References.MOD_ID, "wire")),
                    BlockEntityType.Builder.of(TileElectricWire::new, EMBlocks.blockSuperconductorWireArray.toArray(new Block[0])).build(null).setRegistryName(new ResourceLocation(References.MOD_ID, "wire")),
                    BlockEntityType.Builder.of(TileEMMultipart::new, EMBlocks.multipart.get()).build(null).setRegistryName(new ResourceLocation(References.MOD_ID, "multipart")),
                    BlockEntityType.Builder.of(TileEMMicroblock::new, EMBlocks.microblocks.toArray(new Block[0])).build(null).setRegistryName(new ResourceLocation(References.MOD_ID, "microblock"))
            );

        }
    }*/
}