package tuhljin.automagy.client.renderers;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tuhljin.automagy.common.tiles.TileMawBase;

public class TileMawRenderer extends TileEntitySpecialRenderer<TileMawBase> {
    private static Random rand = new Random();
    private static final ResourceLocation texture1 = new ResourceLocation("thaumcraft", "textures/misc/tunnel.png");
    private static final ResourceLocation texture2 = new ResourceLocation("thaumcraft", "textures/misc/particlefield.png");
    FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);
    private static final double HOLE_SIZE = 0.38D;
    private boolean noloop = false;

    public TileMawRenderer() {
    }

    @Override
    public void render(TileMawBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing facing;
        facing = te.getFacing();
        if (facing == null) {
            facing = EnumFacing.NORTH;
        }

        switch(facing) {
            case DOWN:
                this.drawPlaneBottom(x, y, z);
                break;
            case UP:
                this.drawPlaneTop(x, y, z);
                break;
            case NORTH:
                this.drawPlaneNorth(x, y, z);
                break;
            case SOUTH:
                this.drawPlaneSouth(x, y, z);
                break;
            case WEST:
                this.drawPlaneWest(x, y, z);
                break;
            case EAST:
                this.drawPlaneEast(x, y, z);
        }

    }

    public void drawPlaneBottom(double x, double y, double z) {
        double size = 0.38D;
        double midtrans = (1.0D - size) / 2.0D;
        GL11.glPushMatrix();
        GL11.glTranslated(midtrans, 0.0D, midtrans);
        float f1 = (float)this.rendererDispatcher.entityX;
        float f2 = (float)this.rendererDispatcher.entityY;
        float f3 = (float)this.rendererDispatcher.entityZ;
        GL11.glDisable(2896);
        rand.setSeed(31100L);
        float offset = 0.01F;

        for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(texture1);
                f7 = 0.05F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(texture2);
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float)(-(y + (double)offset));
            float f9 = f8 + (float)ActiveRenderInfo.getCameraPosition().y;
            float f10 = f8 + f5 + (float)ActiveRenderInfo.getCameraPosition().y;
            float f11 = f9 / f10;
            f11 += (float)(y + (double)offset);
            GL11.glTranslatef(f1, f11, f3);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.populateFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.populateFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.populateFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.populateFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f1, -f3, -f2);
            GL11.glTranslatef((float)ActiveRenderInfo.getCameraPosition().x * f5 / f9, (float)ActiveRenderInfo.getCameraPosition().z * f5 / f9, -f2);
            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
            f11 = rand.nextFloat() * 0.5F + 0.1F;
            float f12 = rand.nextFloat() * 0.5F + 0.4F;
            float f13 = rand.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.getBuffer().pos(x, y + (double)offset, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x, y + (double)offset, z + size).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + size, y + (double)offset, z + size).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + size, y + (double)offset, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

    public void drawPlaneTop(double x, double y, double z) {
        double size = 0.38D;
        double midtrans = (1.0D - size) / 2.0D;
        GL11.glPushMatrix();
        GL11.glTranslated(midtrans, 0.89D, midtrans);
        float f1 = (float)this.rendererDispatcher.entityX;
        float f2 = (float)this.rendererDispatcher.entityY;
        float f3 = (float)this.rendererDispatcher.entityZ;
        GL11.glDisable(2896);
        rand.setSeed(31100L);
        float offset = 0.099F;

        for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(texture1);
                f7 = 0.05F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(texture2);
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float)(y + (double)offset);
            float f9 = f8 - (float)ActiveRenderInfo.getCameraPosition().y;
            float f10 = f8 + f5 - (float)ActiveRenderInfo.getCameraPosition().y;
            float f11 = f9 / f10;
            f11 += (float)(y + (double)offset);
            GL11.glTranslatef(f1, f11, f3);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.populateFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.populateFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.populateFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.populateFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f1, -f3, -f2);
            GL11.glTranslatef((float)ActiveRenderInfo.getCameraPosition().x * f5 / f9, (float)ActiveRenderInfo.getCameraPosition().z * f5 / f9, -f2);
            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
            f11 = rand.nextFloat() * 0.5F + 0.1F;
            float f12 = rand.nextFloat() * 0.5F + 0.4F;
            float f13 = rand.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.getBuffer().pos(x, y + (double)offset, z + size).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x, y + (double)offset, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + size, y + (double)offset, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + size, y + (double)offset, z + size).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

    public void drawPlaneNorth(double x, double y, double z) {
        double size = 0.38D;
        double midtrans = (1.0D - size) / 2.0D;
        GL11.glPushMatrix();
        GL11.glTranslated(midtrans, midtrans, 0.0D);
        float f1 = (float)this.rendererDispatcher.entityX;
        float f2 = (float)this.rendererDispatcher.entityY;
        float f3 = (float)this.rendererDispatcher.entityZ;
        GL11.glDisable(2896);
        rand.setSeed(31100L);
        float offset = 0.01F;

        for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(texture1);
                f7 = 0.05F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(texture2);
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float)(-(z + (double)offset));
            float f9 = f8 + (float)ActiveRenderInfo.getCameraPosition().z;
            float f10 = f8 + f5 + (float)ActiveRenderInfo.getCameraPosition().z;
            float f11 = f9 / f10;
            f11 += (float)(z + (double)offset);
            GL11.glTranslatef(f1, f2, f11);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.populateFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.populateFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.populateFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.populateFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f1, -f2, -f3);
            GL11.glTranslatef((float)ActiveRenderInfo.getCameraPosition().x * f5 / f9, (float)ActiveRenderInfo.getCameraPosition().x * f5 / f9, -f3);
            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
            f11 = rand.nextFloat() * 0.5F + 0.1F;
            float f12 = rand.nextFloat() * 0.5F + 0.4F;
            float f13 = rand.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.getBuffer().pos(x, y + size, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x, y, z + (double)offset).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + size, y, z + (double)offset).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + size, y + size, z + (double)offset).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

    public void drawPlaneSouth(double x, double y, double z) {
        double size = 0.38D;
        double midtrans = (1.0D - size) / 2.0D;
        GL11.glPushMatrix();
        GL11.glTranslated(midtrans, midtrans, 0.0D);
        float f1 = (float)this.rendererDispatcher.entityX;
        float f2 = (float)this.rendererDispatcher.entityY;
        float f3 = (float)this.rendererDispatcher.entityZ;
        GL11.glDisable(2896);
        rand.setSeed(31100L);
        float offset = 0.99F;

        for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(texture1);
                f7 = 0.05F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(texture2);
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float)(z + (double)offset);
            float f9 = f8 - (float)ActiveRenderInfo.getCameraPosition().z;
            float f10 = f8 + f5 - (float)ActiveRenderInfo.getCameraPosition().z;
            float f11 = f9 / f10;
            f11 += (float)(z + (double)offset);
            GL11.glTranslatef(f1, f2, f11);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.populateFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.populateFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.populateFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.populateFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f1, -f2, -f3);
            GL11.glTranslatef((float)ActiveRenderInfo.getCameraPosition().x * f5 / f9, (float)ActiveRenderInfo.getCameraPosition().y * f5 / f9, -f3);
            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
            f11 = rand.nextFloat() * 0.5F + 0.1F;
            float f12 = rand.nextFloat() * 0.5F + 0.4F;
            float f13 = rand.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.getBuffer().pos(x, y, z + (double)offset).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x, y + size, z + (double)offset).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + size, y + size, z + (double)offset).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + size, y, z + (double)offset).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

    public void drawPlaneWest(double x, double y, double z) {
        double size = 0.38D;
        double midtrans = (1.0D - size) / 2.0D;
        GL11.glPushMatrix();
        GL11.glTranslated(0.0D, midtrans, midtrans);
        float f1 = (float)this.rendererDispatcher.entityX;
        float f2 = (float)this.rendererDispatcher.entityY;
        float f3 = (float)this.rendererDispatcher.entityZ;
        GL11.glDisable(2896);
        rand.setSeed(31100L);
        float offset = 0.01F;

        for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(texture1);
                f7 = 0.05F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(texture2);
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float)(-(x + (double)offset));
            float f9 = f8 + (float)ActiveRenderInfo.getCameraPosition().x;
            float f10 = f8 + f5 + (float)ActiveRenderInfo.getCameraPosition().x;
            float f11 = f9 / f10;
            f11 += (float)(x + (double)offset);
            GL11.glTranslatef(f11, f2, f3);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8193, 9473, this.populateFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8192, 9473, this.populateFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.populateFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.populateFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f3, -f2, -f1);
            GL11.glTranslatef((float)ActiveRenderInfo.getCameraPosition().z * f5 / f9, (float)ActiveRenderInfo.getCameraPosition().y * f5 / f9, -f1);
            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
            f11 = rand.nextFloat() * 0.5F + 0.1F;
            float f12 = rand.nextFloat() * 0.5F + 0.4F;
            float f13 = rand.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.getBuffer().pos(x + (double)offset, y + size, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + (double)offset, y + size, z + size).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + (double)offset, y, z + size).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + (double)offset, y, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

    public void drawPlaneEast(double x, double y, double z) {
        double size = 0.38D;
        double midtrans = (1.0D - size) / 2.0D;
        GL11.glPushMatrix();
        GL11.glTranslated(0.0D, midtrans, midtrans);
        float f1 = (float)this.rendererDispatcher.entityX;
        float f2 = (float)this.rendererDispatcher.entityY;
        float f3 = (float)this.rendererDispatcher.entityZ;
        GL11.glDisable(2896);
        rand.setSeed(31100L);
        float offset = 0.99F;

        for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
                this.bindTexture(texture1);
                f7 = 0.05F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
                this.bindTexture(texture2);
                GL11.glEnable(3042);
                GL11.glBlendFunc(1, 1);
                f6 = 0.5F;
            }

            float f8 = (float)(x + (double)offset);
            float f9 = f8 - (float)ActiveRenderInfo.getCameraPosition().x;
            float f10 = f8 + f5 - (float)ActiveRenderInfo.getCameraPosition().x;
            float f11 = f9 / f10;
            f11 += (float)(x + (double)offset);
            GL11.glTranslatef(f11, f2, f3);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8193, 9473, this.populateFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8192, 9473, this.populateFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.populateFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.populateFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f3, -f2, -f1);
            GL11.glTranslatef((float)ActiveRenderInfo.getCameraPosition().z * f5 / f9, (float)ActiveRenderInfo.getCameraPosition().y * f5 / f9, -f1);
            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
            f11 = rand.nextFloat() * 0.5F + 0.1F;
            float f12 = rand.nextFloat() * 0.5F + 0.4F;
            float f13 = rand.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.getBuffer().pos(x + (double)offset, y, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + (double)offset, y, z + size).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + (double)offset, y + size, z + size).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.getBuffer().pos(x + (double)offset, y + size, z).color(f11 * f7, f12 * f7, f13 * f7, 1.0F).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }

        GL11.glDisable(3042);
        GL11.glDisable(3168);
        GL11.glDisable(3169);
        GL11.glDisable(3170);
        GL11.glDisable(3171);
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

    private FloatBuffer populateFloatBuffer(float f1, float f2, float f3, float f4) {
        this.floatBuffer.clear();
        this.floatBuffer.put(f1).put(f2).put(f3).put(f4);
        this.floatBuffer.flip();
        return this.floatBuffer;
    }
}
