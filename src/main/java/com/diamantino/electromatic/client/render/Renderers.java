package com.diamantino.electromatic.client.render;

import com.diamantino.electromatic.blocks.BlockEMMicroblock;
import com.diamantino.electromatic.blocks.BlockEMMultipart;
import com.diamantino.electromatic.registration.EMBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author DiamantinoOp
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class Renderers {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt){
        for (Block block : EMBlocks.blockList.stream().map(RegistryObject::get).toArray(Block[]::new)) {
            if ((block instanceof ICustomModelBlock)) {
                registerBakedModel(block);
            }
        }
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {

        //Register Multipart Model
        EMMultipartModel multipartModel = new EMMultipartModel();
        event.getModelRegistry().put(new ModelResourceLocation("electromatic:multipart","waterlogged=false"), multipartModel);
        event.getModelRegistry().put(new ModelResourceLocation("electromatic:multipart","waterlogged=true"), multipartModel);

        EMMicroblockModel microblockModel = new EMMicroblockModel();

        for(Direction dir : Direction.values()) {
            //Register Microblock Models
            /*event.getModelRegistry().put(new ModelResourceLocation("electromatic:half_block", "face=" + dir.getName()), event.getModelRegistry().get(new ModelResourceLocation("electromatic:half_block", "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModelRegistry().put(new ModelResourceLocation("electromatic:half_block", "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("electromatic:half_block", "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("electromatic:panel", "face=" + dir.getName()), event.getModelRegistry().get(new ModelResourceLocation("electromatic:panel", "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModelRegistry().put(new ModelResourceLocation("electromatic:panel", "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("electromatic:panel", "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("electromatic:cover", "face=" + dir.getName()), event.getModelRegistry().get(new ModelResourceLocation("electromatic:cover", "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModelRegistry().put(new ModelResourceLocation("electromatic:cover", "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("electromatic:cover", "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);*/
        }

        /*event.getModelRegistry().put(new ModelResourceLocation("electromatic:half_block", "inventory"), microblockModel);
        event.getModelRegistry().put(new ModelResourceLocation("electromatic:panel", "inventory"), microblockModel);
        event.getModelRegistry().put(new ModelResourceLocation("electromatic:cover", "inventory"), microblockModel);*/

    }

    public static void init() {

        //TODO: Fix

        //BlockEntityRenderers.register(EMBlockEntityTypes.LAMP, context -> new RenderLamp());
        //BlockEntityRenderers.register(EMBlockEntityTypes.ENGINE, context -> new RenderEngine());


        /*for (RegistryObject<Item> item : EMItems.ITEMS.getEntries()) {
            if (item.get() instanceof IEMColoredItem) {
                Minecraft.getInstance().getItemColors().register(new EMItemColor(), item.get());
            }
        }*/
        for (Block block : EMBlocks.blockList.stream().map(RegistryObject::get).toArray(Block[]::new)) {
            if (block instanceof IEMColoredBlock) {
                Minecraft.getInstance().getBlockColors().register(new EMBlockColor(), block);
                Minecraft.getInstance().getItemColors().register(new EMBlockColor(), Item.byBlock(block));
            }
            /*if(block instanceof BlockLampSurface || block instanceof BlockGateBase || block instanceof BlockBattery)
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());*/
            if(/*block instanceof BlockEMGlass || */block instanceof BlockEMMicroblock || block instanceof BlockEMMultipart)
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());

            if(block instanceof BlockEMMicroblock || block instanceof BlockEMMultipart)
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
        }

        /*ItemBlockRenderTypes.setRenderLayer(EMBlocks.indigo_flower, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EMBlocks.flax_crop, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EMBlocks.cracked_basalt_lava, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EMBlocks.cracked_basalt_decorative, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EMBlocks.rubber_leaves, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EMBlocks.rubber_sapling, RenderType.cutout());*/
        //ItemBlockRenderTypes.setRenderLayer(EMBlocks.tube, RenderType.cutout());

    }

    public static void registerBakedModel(Block block) {
        ((ICustomModelBlock) block).initModel();
    }

}