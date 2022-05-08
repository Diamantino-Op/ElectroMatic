package com.diamantino.electromatic.registration;

import com.diamantino.electromatic.References;
import com.diamantino.electromatic.api.misc.MinecraftColor;
import com.diamantino.electromatic.api.multipart.IEMPartBlock;
import com.diamantino.electromatic.api.wire.electricity.ElectricWireType;
import com.diamantino.electromatic.blocks.*;
import com.diamantino.electromatic.items.ItemEMPart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = References.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EMBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MOD_ID);

    //TODO: Fix

    public static List<RegistryObject<?>> blockList = new ArrayList<>();
    public static List<RegistryObject<BlockEMMicroblock>> microblocks = new ArrayList<>();

    public static List<RegistryObject<BlockElectricWire>> blockCopperWireArray = new ArrayList<>();
    public static List<RegistryObject<BlockElectricWire>> blockSilverWireArray = new ArrayList<>();
    public static List<RegistryObject<BlockElectricWire>> blockGoldWireArray = new ArrayList<>();
    public static List<RegistryObject<BlockElectricWire>> blockSuperconductorWireArray = new ArrayList<>();

    public static List<RegistryObject<BlockElectricWire>> blockWireAll = new ArrayList<>();

    public static final RegistryObject<BlockEMMultipart> multipart = registerBlock("multipart", BlockEMMultipart::new);

    public static final RegistryObject<BlockPipe> pipe = registerBlock("pipe", BlockPipe::new);

    static {

        //Wires
        for (MinecraftColor color : MinecraftColor.WIRE_COLORS) {
            String type = ElectricWireType.COPPER.getName();
            RegistryObject<BlockElectricWire> block = registerBlock(MinecraftColor.getName(color) + "_" + type + "_wire", () -> new BlockElectricWire(type, color));
            blockCopperWireArray.add(block);
            blockWireAll.add(block);
        }

        for (MinecraftColor color : MinecraftColor.WIRE_COLORS) {
            String type = ElectricWireType.SILVER.getName();
            RegistryObject<BlockElectricWire> block = registerBlock(MinecraftColor.getName(color) + "_" + type + "_wire", () -> new BlockElectricWire(type, color));
            blockSilverWireArray.add(block);
            blockWireAll.add(block);
        }

        for (MinecraftColor color : MinecraftColor.WIRE_COLORS) {
            String type = ElectricWireType.GOLD.getName();
            RegistryObject<BlockElectricWire> block = registerBlock(MinecraftColor.getName(color) + "_" + type + "_wire", () -> new BlockElectricWire(type, color));
            blockGoldWireArray.add(block);
            blockWireAll.add(block);
        }

        for (MinecraftColor color : MinecraftColor.WIRE_COLORS) {
            String type = ElectricWireType.SUPERCONDUCTOR.getName();
            RegistryObject<BlockElectricWire> block = registerBlock(MinecraftColor.getName(color) + "_" + type + "_wire", () -> new BlockElectricWire(type, color));
            blockSuperconductorWireArray.add(block);
            blockWireAll.add(block);
        }

        //Pipes
        //pipe = new BlockPipe().setRegistryName(References.MOD_ID, "pipe");
    }

    private static RegistryObject<BlockEMMicroblock> registerMicroBlock(String name, Supplier<BlockEMMicroblock> block) {
        RegistryObject<BlockEMMicroblock> toReturn = BLOCKS.register(name, block);
        microblocks.add(toReturn);
        registerBlockItems(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        blockList.add(toReturn);
        registerBlockItems(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItems(String name, RegistryObject<T> block) {
        return EMItems.ITEMS.register(name, () -> {
            if (!(block.get() instanceof BlockCrop)) { // Crops have seeds rather than blocks
                if((block.get() instanceof BlockBase && ((BlockBase)block.get()).getWIP()) || block.get() instanceof BlockEMMultipart || block.get() instanceof BlockEMMicroblock || block.get() instanceof BlockPipe ){
                    if(block.get() instanceof IEMPartBlock) {
                        return new ItemEMPart(block.get(), new Item.Properties());
                    }else{
                        return new BlockItem(block.get(), new Item.Properties());
                    }
                }else{
                    /*CreativeModeTab group = EMCreativeTabs.blocks;
                    if(block instanceof BlockContainerBase){group = EMCreativeTabs.machines;}
                    if(block instanceof BlockLamp){group = EMCreativeTabs.lighting;}
                    if(block instanceof BlockElectricWire){group = EMCreativeTabs.wiring;}
                    if(block instanceof BlockBlulectricCable){group = EMCreativeTabs.wiring;}
                    if(block instanceof BlockGateBase){group = EMCreativeTabs.circuits;}*/

                    CreativeModeTab group = EMCreativeTabs.wiring;

                    if(block.get() instanceof IEMPartBlock){
                        return new ItemEMPart(block.get(), new Item.Properties().tab(group));
                    }else {
                        return new BlockItem(block.get(), new Item.Properties().tab(group));
                    }
                }
            }

            return null;
        });
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}