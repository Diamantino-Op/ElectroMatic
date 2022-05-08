package com.diamantino.electromatic.client.render;

import com.diamantino.electromatic.tiles.TileEMMultipart;
import com.diamantino.electromatic.tiles.electronics.TileElectricWire;
import com.diamantino.electromatic.utils.MultipartUtils;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.VertexTransformer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Uses Multipart IModelData to create a model.
 * @author DiamantinoOp
 */
public class EMMultipartModel implements BakedModel {


    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        Map<BlockState, IModelData> stateInfo = extraData.getData(TileEMMultipart.STATE_INFO);

        if (stateInfo != null) {
            return stateInfo.keySet().stream().flatMap(
                    i -> brd.getBlockModel(i).getQuads(i, side, rand, stateInfo.get(i)).stream().map(
                            q -> stateInfo.get(i).hasProperty(TileElectricWire.COLOR_INFO) ? transform(q, stateInfo.get(i).getData(TileElectricWire.COLOR_INFO)): q
                    )
            ).collect(Collectors.toList());
        }else{
            return Collections.emptyList();
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return Collections.emptyList();
    }


    private static BakedQuad transform(BakedQuad quad, Pair<Integer, Integer> colorPair) {
        BakedQuadBuilder builder = new BakedQuadBuilder();
        final IVertexConsumer consumer = new VertexTransformer(builder) {
            @Override
            public void put(int element, float... data) {
                VertexFormatElement e = this.getVertexFormat().getElements().get(element);
                if(e.getUsage() == VertexFormatElement.Usage.COLOR){

                    int color = quad.getTintIndex() == 2 ? colorPair.getSecond() : colorPair.getFirst();
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

        LightUtil.putBakedQuad(consumer, quad);

        return builder.build();
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
        HitResult rayTraceResult = Minecraft.getInstance().hitResult;
        if(Minecraft.getInstance().player != null && rayTraceResult instanceof BlockHitResult){
            BlockState state = MultipartUtils.getClosestState(Minecraft.getInstance().player, ((BlockHitResult)rayTraceResult).getBlockPos());
            if(state != null)
                return Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getParticleIcon();
        }
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("minecraft:stone", "")).getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

}