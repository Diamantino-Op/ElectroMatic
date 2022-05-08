package com.diamantino.electromatic.datagen;

import com.diamantino.electromatic.ElectroMatic;
import com.diamantino.electromatic.References;
import com.diamantino.electromatic.registration.EMBlocks;
import com.diamantino.electromatic.registration.EMTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockTagsProvider extends BlockTagsProvider {

    protected ModBlockTagsProvider(DataGenerator p_126546_, ExistingFileHelper p_126547_) {
        super(p_126546_, References.MOD_ID, p_126547_);
    }

    @Override
    protected void addTags() {
        this.tag(EMTags.copperWiresBlock).add(EMBlocks.blockCopperWireArray.stream().map(RegistryObject::get).toArray(Block[]::new));
        this.tag(EMTags.silverWiresBlock).add(EMBlocks.blockSilverWireArray.stream().map(RegistryObject::get).toArray(Block[]::new));
        this.tag(EMTags.goldWiresBlock).add(EMBlocks.blockGoldWireArray.stream().map(RegistryObject::get).toArray(Block[]::new));
        this.tag(EMTags.superconductorWiresBlock).add(EMBlocks.blockSuperconductorWireArray.stream().map(RegistryObject::get).toArray(Block[]::new));
    }

    @Override
    public String getName() {
        return "ElectroMatic Block Tags";
    }
}
