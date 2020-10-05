package tuhljin.automagy.client.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import tuhljin.automagy.common.items.ItemGlyph;
import tuhljin.automagy.common.tiles.TileTankThirsty;

public class TileTankThirstyRenderer extends TileEntitySpecialRenderer<TileTankThirsty> {
    public TileTankThirstyRenderer() {
    }

    public void renderTileEntityAt(TileTankThirsty te, double x, double y, double z, float partialTicks, int destroyStage) {
        FluidStack fluidStack = te.tank.getFluid();
        if (fluidStack != null) {
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            GlStateManager.disableBlend();
            GL11.glTranslated(0.5D, 0.45D, 0.5D);
            GL11.glScaled(0.624D, 0.624D, 0.624D);
            GL11.glDisable(3042);
            this.renderFluid(fluidStack, te.getFractionFullRendered());
            GL11.glPopMatrix();
        }

        boolean renderShifted = false;

        for(EnumFacing side : EnumFacing.values()) {
            int glyph = te.getGlyph(side);
            if (glyph != 0) {
                if (!renderShifted) {
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);
                    renderShifted = true;
                }

                this.renderGlyph(side, glyph, te);
            }
        }

        if (renderShifted) {
            GL11.glPopMatrix();
        }

    }

    protected static void addVertexWithUV(BufferBuilder bb, double x, double y, double z, double u, double v) {
        bb.pos(x, y, z).tex(u, v).endVertex();
    }

    protected static void addVertexWithUVAdj(BufferBuilder bb, double x, double y, double z, double u, double v) {
        addVertexWithUV(bb, x - 0.5D, y - 0.5D, z - 0.5D, u, v);
    }

    public void renderFluid(FluidStack fluidStack, double height) {
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Fluid fluid = fluidStack.getFluid();
        TextureAtlasSprite texture = getFluidTexture(fluidStack);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        int color = fluid.getColor(fluidStack);

        height *= 1.18D;
        double uMin = (double)texture.getMinU();
        double uMax = (double)texture.getMaxU();
        double vMin = (double)texture.getMinV();
        double vMax = (double)texture.getMaxV();
        double heightBtm = Math.min(height, 1.0D);
        double adjHeightBtm = (vMax - vMin) * heightBtm;
        double adjHeightTop = height > 1.0D ? (vMax - vMin) * (height - 1.0D) : 0.0D;
        setColor(color);
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();
        bb.begin(7, DefaultVertexFormats.POSITION_TEX);
        addVertexWithUVAdj(bb, 1.0D, 0.0D, 0.0D, uMax, vMin);
        addVertexWithUVAdj(bb, 0.0D, 0.0D, 0.0D, uMin, vMin);
        addVertexWithUVAdj(bb, 0.0D, heightBtm, 0.0D, uMin, vMin + adjHeightBtm);
        addVertexWithUVAdj(bb, 1.0D, heightBtm, 0.0D, uMax, vMin + adjHeightBtm);
        if (height > 1.0D) {
            addVertexWithUVAdj(bb, 1.0D, 1.0D, 0.0D, uMax, vMin);
            addVertexWithUVAdj(bb, 0.0D, 1.0D, 0.0D, uMin, vMin);
            addVertexWithUVAdj(bb, 0.0D, height, 0.0D, uMin, vMin + adjHeightTop);
            addVertexWithUVAdj(bb, 1.0D, height, 0.0D, uMax, vMin + adjHeightTop);
        }

        addVertexWithUVAdj(bb, 1.0D, 0.0D, 1.0D, uMin, vMin);
        addVertexWithUVAdj(bb, 1.0D, heightBtm, 1.0D, uMin, vMin + adjHeightBtm);
        addVertexWithUVAdj(bb, 0.0D, heightBtm, 1.0D, uMax, vMin + adjHeightBtm);
        addVertexWithUVAdj(bb, 0.0D, 0.0D, 1.0D, uMax, vMin);
        if (height > 1.0D) {
            addVertexWithUVAdj(bb, 1.0D, 1.0D, 1.0D, uMin, vMin);
            addVertexWithUVAdj(bb, 1.0D, height, 1.0D, uMin, vMin + adjHeightTop);
            addVertexWithUVAdj(bb, 0.0D, height, 1.0D, uMax, vMin + adjHeightTop);
            addVertexWithUVAdj(bb, 0.0D, 1.0D, 1.0D, uMax, vMin);
        }

        addVertexWithUVAdj(bb, 1.0D, 0.0D, 0.0D, uMin, vMin);
        addVertexWithUVAdj(bb, 1.0D, heightBtm, 0.0D, uMin, vMin + adjHeightBtm);
        addVertexWithUVAdj(bb, 1.0D, heightBtm, 1.0D, uMax, vMin + adjHeightBtm);
        addVertexWithUVAdj(bb, 1.0D, 0.0D, 1.0D, uMax, vMin);
        if (height > 1.0D) {
            addVertexWithUVAdj(bb, 1.0D, 1.0D, 0.0D, uMin, vMin);
            addVertexWithUVAdj(bb, 1.0D, height, 0.0D, uMin, vMin + adjHeightTop);
            addVertexWithUVAdj(bb, 1.0D, height, 1.0D, uMax, vMin + adjHeightTop);
            addVertexWithUVAdj(bb, 1.0D, 1.0D, 1.0D, uMax, vMin);
        }

        addVertexWithUVAdj(bb, 0.0D, 0.0D, 1.0D, uMin, vMin);
        addVertexWithUVAdj(bb, 0.0D, heightBtm, 1.0D, uMin, vMin + adjHeightBtm);
        addVertexWithUVAdj(bb, 0.0D, heightBtm, 0.0D, uMax, vMin + adjHeightBtm);
        addVertexWithUVAdj(bb, 0.0D, 0.0D, 0.0D, uMax, vMin);
        if (height > 1.0D) {
            addVertexWithUVAdj(bb, 0.0D, 1.0D, 1.0D, uMin, vMin);
            addVertexWithUVAdj(bb, 0.0D, height, 1.0D, uMin, vMin + adjHeightTop);
            addVertexWithUVAdj(bb, 0.0D, height, 0.0D, uMax, vMin + adjHeightTop);
            addVertexWithUVAdj(bb, 0.0D, 1.0D, 0.0D, uMax, vMin);
        }

        double uMid = (uMax + uMin) / 2.0D;
        double vMid = (vMax + vMin) / 2.0D;
        addVertexWithUVAdj(bb, 0.5D, height, 0.5D, uMid, vMid);
        addVertexWithUVAdj(bb, 1.0D, height, 1.0D, uMax, vMin);
        addVertexWithUVAdj(bb, 1.0D, height, 0.0D, uMin, vMin);
        addVertexWithUVAdj(bb, 0.0D, height, 0.0D, uMin, vMax);
        addVertexWithUVAdj(bb, 0.0D, height, 1.0D, uMax, vMax);
        addVertexWithUVAdj(bb, 1.0D, height, 1.0D, uMax, vMin);
        addVertexWithUVAdj(bb, 0.5D, height, 0.5D, uMid, vMid);
        addVertexWithUVAdj(bb, 0.0D, height, 0.0D, uMin, vMax);
        tes.draw();
        GlStateManager.enableLighting();
    }

    public void renderGlyph(EnumFacing side, int glyph, TileTankThirsty te) {
        GL11.glPushMatrix();
        switch(side) {
            case DOWN:
                GL11.glTranslatef(0.0F, -0.187F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case UP:
                GL11.glTranslatef(0.0F, 1.063F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case NORTH:
            default:
                GL11.glTranslatef(1.0F, 1.0F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                break;
            case SOUTH:
                GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                break;
            case WEST:
                GL11.glTranslatef(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                break;
            case EAST:
                GL11.glTranslatef(1.0F, 1.0F, 1.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.125F, 0.125F, 0.0F);
        GL11.glTranslated(0.25D, 0.25D, 0.187D);
        TextureAtlasSprite texture = ItemGlyph.getGlyphTexture(glyph);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        double uMin = (double)texture.getMinU();
        double uMax = (double)texture.getMaxU();
        double vMin = (double)texture.getMinV();
        double vMax = (double)texture.getMaxV();
        double height = 1.0D;
        double adjHeight = (vMax - vMin) * height;
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();
        bb.begin(7, DefaultVertexFormats.POSITION_TEX);
        height /= 4.0D;
        addVertexWithUV(bb, 0.25D, 0.0D, 0.0D, uMax, vMin);
        addVertexWithUV(bb, 0.0D, 0.0D, 0.0D, uMin, vMin);
        addVertexWithUV(bb, 0.0D, height, 0.0D, uMin, vMin + adjHeight);
        addVertexWithUV(bb, 0.25D, height, 0.0D, uMax, vMin + adjHeight);
        tes.draw();
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }

    private void renderFloatingItem(World world, ItemStack stack, double x, double y, double z, int destroyStage) {
        EntityItem entityitem = null;
        float ticks = (float)(Minecraft.getMinecraft().getRenderViewEntity().ticksExisted + destroyStage);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.2F, (float)z + 0.5F);
        GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
        ItemStack is = stack.copy();
        is.setCount(1);
        entityitem = new EntityItem(world, 0.0D, 0.0D, 0.0D, is);
        entityitem.hoverStart = 0.0F;
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.renderEntity(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
        GL11.glPopMatrix();
    }

    public static TextureAtlasSprite getFluidTexture(FluidStack fluid) {
        ResourceLocation textureLocation = fluid.getFluid().getStill(fluid);
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(textureLocation.toString());
    }

    public static void setColor(int rgb) {
        float r = (float)(rgb >> 16 & 255) / 255.0F;
        float g = (float)(rgb >> 8 & 255) / 255.0F;
        float b = (float)(rgb >> 0 & 255) / 255.0F;
        GlStateManager.color(r, g, b);
    }
}
