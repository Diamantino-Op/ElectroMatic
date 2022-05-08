package com.diamantino.electromatic.events;

import com.diamantino.electromatic.blocks.BlockEMMultipart;
import com.diamantino.electromatic.utils.MultipartUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EMEventHandler {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event) {

        if (event.getItemStack().hasTag() && event.getItemStack().getTag().contains("tileData")
                && !event.getItemStack().getTag().getBoolean("hideSilkyTooltip")) {
            event.getToolTip().add(new TextComponent("gui.tooltip.hasSilkyData"));
        }

        /*if (ClientProxy.getOpenedGui() instanceof GuiCircuitDatabaseSharing) {
            ItemStack deletingStack = ((GuiCircuitDatabaseSharing) ClientProxy.getOpenedGui()).getCurrentDeletingTemplate();
            if (!deletingStack.isEmpty() && deletingStack == event.getItemStack()) {
                event.getToolTip().add(new TextComponent("gui.circuitDatabase.info.sneakClickToConfirmDeleting"));
            } else {
                event.getToolTip().add(new TextComponent("gui.circuitDatabase.info.sneakClickToDelete"));
            }
        }*/
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {

        Item item = event.getCrafting().getItem();
        if (item == Item.byBlock(Blocks.AIR))
            return;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void blockHighlightEvent(DrawSelectionEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        Level world = player.level;
        HitResult mop = event.getTarget();
        if(mop instanceof BlockHitResult) {
            BlockPos pos = ((BlockHitResult) mop).getBlockPos();
            BlockState state = world.getBlockState(pos);
            if(state.getBlock() instanceof BlockEMMultipart){
                BlockState partstate = MultipartUtils.getClosestState(player, pos);
                VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.lines());
                if(partstate != null) {
                    VoxelShape shape = partstate.getShape(world, pos, CollisionContext.of(player));
                    Vec3 projectedView = event.getCamera().getPosition();
                    double d0 = pos.getX() - projectedView.x();
                    double d1 = pos.getY() - projectedView.y();
                    double d2 = pos.getZ() - projectedView.z();
                    Matrix4f matrix4f = event.getPoseStack().last().pose();
                    //shape.forAllEdges((startX, startY, startZ, endX, endY, endZ) -> {
                    //    builder.vertex(matrix4f, (float)(startX + d0), (float)(startY + d1), (float)(startZ + d2)).color(0.0F, 0.0F, 0.0F, 0.4F).endVertex();
                    //    builder.vertex(matrix4f, (float)(endX + d0), (float)(endY + d1), (float)(endZ + d2)).color(0.0F, 0.0F, 0.0F, 0.4F).endVertex();
                    //});
                    event.setCanceled(true);
                }
            }
        }
    }
}
