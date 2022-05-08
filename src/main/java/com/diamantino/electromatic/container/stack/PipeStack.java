package com.diamantino.electromatic.container.stack;

import com.diamantino.electromatic.api.pipe.IVacuumPipe;
import com.diamantino.electromatic.client.render.RenderHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author DiamantinoOp
 */

public class PipeStack {

    public ItemStack stack;
    public final IVacuumPipe.PipeColor color;
    public double progress; // 0 at the start, 0.5 on an intersection, 1 at the end.
    public double oldProgress;
    public Direction heading;
    public boolean enabled = true; // will be disabled when the client sided stack is at an intersection, at which point it needs to wait for server
    // input. This just serves a visual purpose.
    public int idleCounter; // increased when the stack is standing still. This will cause the client to remove the stack when a timeout occurs.
    private BlockEntity target; // only should have a value when retrieving items. this is the target the item wants to go to.
    private int targetX, targetY, targetZ;
    public static final double ITEM_SPEED = 0.0625;
    private double speed = ITEM_SPEED;
    public static double tickTimeMultiplier = 1;//Used client side to correct for TPS lag. This is being synchronized from the server.

    @OnlyIn(Dist.CLIENT)
    private static ItemRenderer customRenderItem;
    private static ItemEntity renderedItem;

    public static RenderMode renderMode;

    public static enum RenderMode {
        AUTO, NORMAL, REDUCED, NONE
    }

    public PipeStack(ItemStack stack, Direction from) {

        this(stack, from, IVacuumPipe.PipeColor.NONE);
    }

    public PipeStack(ItemStack stack, Direction from, IVacuumPipe.PipeColor color) {

        heading = from;
        this.stack = stack;
        this.color = color;
    }

    public void setSpeed(double speed) {

        this.speed = speed;
    }

    public double getSpeed() {

        return speed;
    }

    /**
     * Updates the movement by the given m/tick.
     * @return true if the stack has gone past the center, meaning logic needs to be triggered.
     */
    public boolean update(Level worldObj) {

        oldProgress = progress;
        if (enabled) {
            boolean isEntering = progress < 0.5;
            progress += speed * (worldObj.isClientSide ? tickTimeMultiplier : 1);
            return progress >= 0.5 && isEntering;
        } else {
            idleCounter++;
            return false;
        }
    }

    public BlockEntity getTarget(Level world) {

        if (target == null && (targetX != 0 || targetY != 0 || targetZ != 0)) {
            target = world.getBlockEntity(new BlockPos(targetX, targetY, targetZ));
        }
        return target;
    }


    public void setTarget(BlockEntity tileEntity) {
        target = tileEntity;
        if (target != null) {
            targetX = target.getBlockPos().getX();
            targetY = target.getBlockPos().getY();
            targetZ = target.getBlockPos().getZ();
        } else {
            targetX = 0;
            targetY = 0;
            targetZ = 0;
        }
    }

    public PipeStack copy() {

        CompoundTag tag = new CompoundTag();
        writeToNBT(tag);
        return loadFromNBT(tag);
    }

    public void writeToNBT(CompoundTag tag) {

        stack.save(tag);
        tag.putByte("color", (byte) color.ordinal());
        tag.putByte("heading", (byte) heading.ordinal());
        tag.putDouble("progress", progress);
        tag.putDouble("speed", speed);
        tag.putInt("targetX", targetX);
        tag.putInt("targetY", targetY);
        tag.putInt("targetZ", targetZ);
    }

    public static PipeStack loadFromNBT(CompoundTag tag) {

        PipeStack stack = new PipeStack(ItemStack.of(tag), Direction.from3DDataValue(tag.getByte("heading")),
                IVacuumPipe.PipeColor.values()[tag.getByte("color")]);
        stack.progress = tag.getDouble("progress");
        stack.speed = tag.getDouble("speed");
        stack.targetX = tag.getInt("targetX");
        stack.targetY = tag.getInt("targetY");
        stack.targetZ = tag.getInt("targetZ");
        return stack;
    }

    public void writeToPacket(ByteBuf buf) {
        //TODO: Add Items
        //ByteBufUtils.writeItemStack(buf, stack);
        buf.writeByte(heading.ordinal());
        buf.writeByte((byte) color.ordinal());
        buf.writeDouble(speed);
        buf.writeDouble(progress);
    }

    public static PipeStack loadFromPacket(ByteBuf buf) {
        //TODO: Add Items
        //ByteBufUtils.readItemStack(buf)
        PipeStack stack = new PipeStack(null, Direction.from3DDataValue(buf.readByte()),
                IVacuumPipe.PipeColor.values()[buf.readByte()]);
        stack.speed = buf.readDouble();
        stack.progress = buf.readDouble();
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(float partialTick) {

        if (renderMode == RenderMode.AUTO) {
            renderMode = Minecraft.getInstance().options.graphicsMode == GraphicsStatus.FANCY ? RenderMode.NORMAL : RenderMode.REDUCED;
        }
        final RenderMode finalRenderMode = renderMode;

        if (customRenderItem == null) {
            customRenderItem = Minecraft.getInstance().getItemRenderer();

            renderedItem = new ItemEntity(Minecraft.getInstance().level, 0,0,0, ItemStack.EMPTY);
        }

        double renderProgress = (oldProgress + (progress - oldProgress) * partialTick) * 2 - 1;

        GL11.glPushMatrix();
        GL11.glTranslated(heading.getStepX() * renderProgress * 0.5, heading.getStepY() * renderProgress * 0.5, heading.getStepZ() * renderProgress * 0.5);
        if (finalRenderMode != RenderMode.NONE) {
            GL11.glPushMatrix();
            if (stack.getCount() > 5) {
                GL11.glScaled(0.8, 0.8, 0.8);
            }
            if (!(stack.getItem() instanceof BlockItem)) {
                GL11.glScaled(0.8, 0.8, 0.8);
                GL11.glTranslated(0, -0.15, 0);
            }

            //TODO: customRenderItem.renderItem(stack, ItemTransforms.TransformType.GROUND);
            GL11.glPopMatrix();
        } else {
            float size = 0.02F;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.drawColoredCube(new AABB(-size, -size, -size, size, size, size), 1, 1, 1, 1);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        if (color != IVacuumPipe.PipeColor.NONE) {

            float size = 0.2F;

            int colorInt = DyeColor.values()[color.ordinal()].getId();
            float red = (colorInt >> 16) / 256F;
            float green = (colorInt >> 8 & 255) / 256F;
            float blue = (colorInt & 255) / 256F;

            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor3f(red, green, blue);
            //TODO: Find replacement for RenderEngine
            //Minecraft.getInstance().renderEngine.bindTexture(new ResourceLocation(Refs.MODID, "textures/blocks/tubes/inside_color_border.png"));
            RenderHelper.drawTesselatedTexturedCube(new AABB(-size, -size, -size, size, size, size));
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glPopMatrix();
    }
}