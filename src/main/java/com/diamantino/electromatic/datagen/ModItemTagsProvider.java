package com.diamantino.electromatic.datagen;

import com.diamantino.electromatic.ElectroMatic;
import com.diamantino.electromatic.References;
import com.diamantino.electromatic.registration.EMTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
    protected ModItemTagsProvider(DataGenerator p_126546_, ExistingFileHelper p_126547_, ModBlockTagsProvider blockTagsProvider) {
        super(p_126546_, blockTagsProvider, References.MOD_ID, p_126547_);
    }

    @Override
    protected void addTags() {
        this.copy(EMTags.copperWiresBlock, EMTags.copperWiresItem);
        this.copy(EMTags.silverWiresBlock, EMTags.silverWiresItem);
        this.copy(EMTags.goldWiresBlock, EMTags.goldWiresItem);
        this.copy(EMTags.superconductorWiresBlock, EMTags.superconductorWiresItem);
    }

    @Override
    public String getName() {
        return "ElectroMatic Item Tags";
    }
}
