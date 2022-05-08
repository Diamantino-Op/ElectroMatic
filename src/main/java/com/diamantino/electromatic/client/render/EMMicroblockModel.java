package com.diamantino.electromatic.client.render;

import com.diamantino.electromatic.blocks.BlockEMMicroblock;
import com.diamantino.electromatic.registration.EMBlocks;
import com.diamantino.electromatic.tiles.TileEMMicroblock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TransformationHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Uses Microblock IModelData to create a model.
 * @author DiamantinoOp
 */
public class EMMicroblockModel implements BakedModel {
    //TODO: Fix

    //private Block defBlock = EMBlocks.marble;
    //private Block defSize = EMBlocks.half_block;

    private Block defBlock = null;
    private Block defSize = null;

    EMMicroblockModel(){}

    private EMMicroblockModel(Block defBlock, Block defSize){
        this.defBlock = defBlock;
        this.defSize = defSize;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        Pair<Block, Integer> info = extraData.getData(TileEMMicroblock.PROPERTY_INFO);
        if (info != null) {
            BakedModel typeModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(info.getKey().defaultBlockState());
            BakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getRegistryName(), "face=" + Direction.WEST));

            List<BakedQuad> bakedQuads = new ArrayList<>();

            if(state != null && state.getBlock() instanceof BlockEMMicroblock) {
                sizeModel = Minecraft.getInstance().getModelManager().getModel(
                        new ModelResourceLocation(state.getBlock().getRegistryName(), "face=" + state.getValue(BlockEMMicroblock.FACING))
                );
            }

            List<BakedQuad> sizeModelQuads = sizeModel.getQuads(state, side, rand);

            if (state != null) {
                TextureAtlasSprite sprite = typeModel.getParticleIcon();
                for (BakedQuad quad: sizeModelQuads) {
                    List<BakedQuad> typeModelQuads = typeModel.getQuads(info.getKey().defaultBlockState(), quad.getDirection(), rand);
                    if(typeModelQuads.size() > 0){
                        sprite = typeModelQuads.get(0).getSprite();
                    }

                    bakedQuads.add(transform(quad, sprite, state.getValue(BlockEMMicroblock.FACING), defBlock));
                }
                return bakedQuads;
            }

        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        List<BakedQuad> outquads = new ArrayList<>();
        BakedModel typeModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(this.defBlock.defaultBlockState());
        BakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getRegistryName(), "face=" + Direction.WEST));

        if(state != null && state.getBlock() instanceof BlockEMMicroblock) {
            sizeModel = Minecraft.getInstance().getModelManager().getModel(
                    new ModelResourceLocation(state.getBlock().getRegistryName(), "face=" + state.getValue(BlockEMMicroblock.FACING))
            );
        }

        List<BakedQuad> sizeModelQuads = sizeModel.getQuads(state, side, rand);

        TextureAtlasSprite sprite = typeModel.getParticleIcon();
        for (BakedQuad quad: sizeModelQuads) {
            List<BakedQuad> typeModelQuads = typeModel.getQuads(this.defBlock.defaultBlockState(), quad.getDirection(), rand);
            if(typeModelQuads.size() > 0){
                sprite = typeModelQuads.get(0).getSprite();
            }

            outquads.add(transform(quad, sprite, Direction.EAST , defBlock));
        }

        return outquads;
    }

    private static BakedQuad transform(BakedQuad sizeQuad, TextureAtlasSprite sprite, Direction dir, Block block) {
        BakedQuadBuilder builder = new BakedQuadBuilder();
        final IVertexConsumer consumer = new VertexTransformer(builder) {
            @Override
            public void put(int element, float... data) {
                VertexFormatElement e = this.getVertexFormat().getElements().get(element);
                if (e.getUsage() == VertexFormatElement.Usage.UV && e.getIndex() == 0) {
                    Vec2 vec = new Vec2(data[0], data[1]);
                    float u = (vec.x - sizeQuad.getSprite().getU0()) / (sizeQuad.getSprite().getU1() - sizeQuad.getSprite().getU0()) * 16;
                    float v = (vec.y - sizeQuad.getSprite().getV0()) / (sizeQuad.getSprite().getV1() - sizeQuad.getSprite().getV0()) * 16;
                    builder.put(element, sprite.getU(u), sprite.getV(v));
                }else if(e.getUsage() == VertexFormatElement.Usage.COLOR){

                    int color;
                    try {
                        color = Minecraft.getInstance().getBlockColors().getColor(block.defaultBlockState(), null, null, sizeQuad.getTintIndex());
                    } catch(Exception ex){
                        try {
                            color = Minecraft.getInstance().getBlockColors().getColor(block.defaultBlockState(), null, BlockPos.ZERO, sizeQuad.getTintIndex());
                        } catch (Exception ex2){
                            color = 0;
                        }
                    }
                    int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
                    int r = (color & redMask) >> 16;
                    int g = (color & greenMask) >> 8;
                    int b = (color & blueMask);

                    parent.put(element, r/255F, g/255F, b/255F, 1);
                }else {
                    parent.put(element, data);
                }
            }
        };
        LightUtil.putBakedQuad(consumer, sizeQuad);
        return builder.build();
    }

    @Override
    public boolean doesHandlePerspectives() {
        return true;
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType type, PoseStack stack) {
        BakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getRegistryName(), "face=" + Direction.WEST));
        Transformation tr = TransformationHelper.toTransformation(sizeModel.getTransforms().getTransform(type));
        if(!tr.isIdentity()) {
            tr.push(stack);
        }
        return this;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        BakedModel typeModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(this.defBlock.defaultBlockState());
        return typeModel.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@Nonnull IModelData data) {
        Pair<Block, Integer> info = data.getData(TileEMMicroblock.PROPERTY_INFO);
        if(info != null)
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(info.getKey().defaultBlockState()).getParticleIcon();
        return getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return new BakedMicroblockOverrideHandler();
    }

    /**
     * Overwrites the model with NBT definition
     */
    private static final class BakedMicroblockOverrideHandler extends ItemOverrides{


        @Nullable
        @Override
        public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int par1){
            CompoundTag nbt = stack.getTag();
            if(nbt != null && nbt.contains("block")){
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
                return new EMMicroblockModel(block, Block.byItem(stack.getItem()));
            }
            return new EMMicroblockModel(Blocks.STONE, Block.byItem(stack.getItem()));
        }
    }

}