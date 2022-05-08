package com.diamantino.electromatic.registration;

import com.diamantino.electromatic.api.misc.MinecraftColor;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class EMCreativeTabs {

    public static CreativeModeTab blocks;
    public static CreativeModeTab machines;
    public static CreativeModeTab items;
    public static CreativeModeTab tools;
    public static CreativeModeTab circuits;
    public static CreativeModeTab wiring;
    public static CreativeModeTab lighting;
    public static CreativeModeTab microblocks;

    static {

        //TODO: Fix

        /*blocks = new EMCreativeTab("electromatic/blocks") {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                Block iconBlock = EMBlocks.amethyst_ore;
                if (iconBlock != null) {
                    return new ItemStack(iconBlock);
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        machines = new EMCreativeTab("electromatic/machines") {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

                Block iconBlock = EMBlocks.alloyfurnace;
                if (iconBlock != null) {
                    return new ItemStack(iconBlock);
                } else {
                    return new ItemStack(Blocks.FURNACE);
                }
            }
        };

        items = new EMCreativeTab("electromatic/items") {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

                Item iconItem = EMItems.ruby_gem.get();
                if (iconItem != null) {
                    return new ItemStack(EMItems.ruby_gem.get());
                } else {
                    return new ItemStack(Items.DIAMOND);
                }
            }
        };

        tools = new EMCreativeTab("electromatic/tools") {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

                Item iconItem = EMItems.screwdriver.get();
                if (iconItem != null) {
                    return new ItemStack(EMItems.screwdriver.get());
                } else {
                    return new ItemStack(Items.DIAMOND_PICKAXE);
                }
            }
        };

        circuits = new EMCreativeTab("electromatic:circuits", true) {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

                ItemStack iconItem = new ItemStack(EMItems.redstone_pointer_tile.get());
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };*/

        wiring = new EMCreativeTab("electromatic/wiring", true) {

            @Override
            @OnlyIn(Dist.CLIENT)
            public @NotNull ItemStack makeIcon() {
                ItemStack iconItem = new ItemStack(EMBlocks.blockCopperWireArray.get(0).get());
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        /*lighting = new EMCreativeTab("electromatic/lighting", true) {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                int t = 1000;

                int i = (int) ((System.currentTimeMillis() / t) % MinecraftColor.VALID_COLORS.length);
                boolean b = ((System.currentTimeMillis() / t) % (MinecraftColor.VALID_COLORS.length * 2L)) >= MinecraftColor.VALID_COLORS.length;
                boolean b2 = ((System.currentTimeMillis() / t) % (MinecraftColor.VALID_COLORS.length * 4L)) >= MinecraftColor.VALID_COLORS.length;

                ItemStack iconItem = new ItemStack(EMBlocks.blockLampRGB);
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        microblocks = new EMCreativeTab("electromatic/microblocks", true) {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                ItemStack iconItem = new ItemStack(EMBlocks.microblocks.get(0));
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> items) {
                for (Block block : ForgeRegistries.BLOCKS.getValues().stream().filter(b -> !(b instanceof EntityBlock)).toList()) {
                    VoxelShape shape = null;
                    try{
                        shape = block.defaultBlockState().getShape(null, null);
                    }catch (NullPointerException ignored){
                        //Shulker Boxes try to query the Tile Entity
                    }
                    if(block.getRegistryName() != null && shape == Shapes.block()) {
                        for (Block mb : EMBlocks.microblocks){
                            CompoundTag nbt = new CompoundTag();
                            nbt.putString("block", block.getRegistryName().toString());
                            ItemStack stack = new ItemStack(mb);
                            stack.setTag(nbt);
                            stack.setHoverName(new TranslatableComponent(block.getDescriptionId())
                                    .append(new TextComponent(" "))
                                    .append(new TranslatableComponent(mb.getDescriptionId())));
                            items.add(stack);
                        }
                    }
                }
            }
        };*/

    }

    private static abstract class EMCreativeTab extends CreativeModeTab {

        private boolean searchbar = false;

        public EMCreativeTab(String label) {

            super(label);
        }

        public EMCreativeTab(String label, boolean searchbar) {

            this(label);
            this.searchbar = searchbar;
        }

        @Override
        public boolean hasSearchBar() {

            return searchbar;
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public @NotNull String getBackgroundSuffix() {

            return searchbar ? "em_search.png" : super.getBackgroundSuffix();
        }

        @Override
        public int getSearchbarWidth() {

            return 62;
        }

    }
}