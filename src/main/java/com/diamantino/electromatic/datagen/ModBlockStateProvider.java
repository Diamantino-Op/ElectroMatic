package com.diamantino.electromatic.datagen;

import com.diamantino.electromatic.References;
import com.diamantino.electromatic.blocks.BlockEMWireBase;
import com.diamantino.electromatic.blocks.BlockElectricWire;
import com.diamantino.electromatic.registration.EMBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, References.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockEMWireBase block : EMBlocks.blockCopperWireArray.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)) {
            makeWire(block);
        }

        for (BlockEMWireBase block : EMBlocks.blockSilverWireArray.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)) {
            makeWire(block);
        }

        for (BlockEMWireBase block : EMBlocks.blockGoldWireArray.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)) {
            makeWire(block);
        }

        for (BlockEMWireBase block : EMBlocks.blockSuperconductorWireArray.stream().map(RegistryObject::get).toArray(BlockElectricWire[]::new)) {
            makeWire(block);
        }
    }

    public void makeWire(BlockEMWireBase block) {
        String name = block.getRegistryName().toString();
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

        List<Direction> directions = new ArrayList<>(BlockEMWireBase.FACING.getPossibleValues());

        directions.forEach(direction -> {
            if (direction == Direction.NORTH) {
                builder.part().modelFile(getWireModelFromExisting("wire")).rotationX(90).addModel().condition(BlockEMWireBase.FACING, direction);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(-90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(-90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_RIGHT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(-90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(-90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_RIGHT, true);
            } else if (direction == Direction.SOUTH) {
                builder.part().modelFile(getWireModelFromExisting("wire")).rotationX(90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(-90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(-90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_RIGHT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(-90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(90).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(-90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_RIGHT, true);
            } else if (direction == Direction.WEST) {
                builder.part().modelFile(getWireModelFromExisting("wire")).rotationX(90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(-90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(-90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_RIGHT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(-90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(-90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_RIGHT, true);
            } else if (direction == Direction.EAST) {
                builder.part().modelFile(getWireModelFromExisting("wire")).rotationX(90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(-90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(-90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_RIGHT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(-90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(90).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(-90).rotationY(270).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_RIGHT, true);
            } else if (direction == Direction.UP) {
                builder.part().modelFile(getWireModelFromExisting("wire")).addModel().condition(BlockEMWireBase.FACING, direction);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationY(-90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_RIGHT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationY(-90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_RIGHT, true);
            } else if (direction == Direction.DOWN) {
                builder.part().modelFile(getWireModelFromExisting("wire")).rotationX(180).addModel().condition(BlockEMWireBase.FACING, direction);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(180).rotationY(-90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended_z")).rotationX(180).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(180).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_extended")).rotationX(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.CONNECTED_RIGHT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(180).rotationY(-90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_FRONT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join_z")).rotationX(180).rotationY(90).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_BACK, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(180).rotationY(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_LEFT, true);
                builder.part().modelFile(getWireModelFromExisting("wire_join")).rotationX(180).addModel().condition(BlockEMWireBase.FACING, direction).condition(BlockEMWireBase.JOIN_RIGHT, true);
            }
        });

        simpleBlockItem(block, models().withExistingParent(name, new ResourceLocation(References.MOD_ID, "block/wire")));
    }

    public ModelFile getWireModelFromExisting(String name) {
        return models().getExistingFile(new ResourceLocation(References.MOD_ID, "block/wire/" + name));
    }
}
