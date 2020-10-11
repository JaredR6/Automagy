//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package tuhljin.automagy.client.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.tiles.TileHourglass;
import tuhljin.automagy.common.tiles.TileHourglassMagic;

public class TileHourglassRenderer extends TileEntitySpecialRenderer<TileHourglass> {
    public static final String OBJ_MODEL = "models/obj/hourglass.obj";
    public static final String TEXTURE_STANDARD = "textures/models/hourglass.png";
    public static final String TEXTURE_MAGIC = "textures/models/hourglass-gold.png";
    public static final String TEXTURE_GLASS = "textures/models/hourglass-glass.png";
    public static final String TEXTURE_SAND = "textures/models/hourglass-sand.png";
    public static final String TEXTURE_REDSTONE = "textures/models/hourglass-redstone.png";
    protected final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(References.MOD_ID, "models/obj/hourglass.obj"));

    @Override
    public void render(TileHourglass te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        boolean renderingItem = ItemBlockSpecialRenderer.isRendering;
        boolean isMagic = renderingItem ? ItemBlockSpecialRenderer.currentBlock == ModBlocks.hourglassMagic : te instanceof TileHourglassMagic;
        String texture = isMagic ? "textures/models/hourglass-gold.png" : "textures/models/hourglass.png";
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        if (renderingItem) {
            GL11.glTranslated(x + 0.5D, y + 0.4D, z + 0.5D);
            GL11.glScaled(0.4D, 0.4D, 0.4D);
        } else {
            GL11.glTranslated(x + 0.5D, y + 0.2D, z + 0.5D);
            GL11.glScaled(0.2D, 0.2D, 0.2D);
        }

        EnumFacing facing = (renderingItem || te.getFacing() == null) ? EnumFacing.NORTH : te.getFacing();
        switch(facing) {
            case NORTH:
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                break;
            case WEST:
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            case EAST:
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MOD_ID, texture));
        this.model.renderOnly("Right_Pillar_group", "group_Bottom", "group_Left_Pillar", "group_Top");
        if (!isMagic) {
            this.model.renderOnly("group_Left_Turner", "group_Right_Turner");
        } else {
            if (!renderingItem && te.getRedstoneSignalStrength() > 0) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
            } else {
                GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.9F);
            }

            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MOD_ID, "textures/models/hourglass-redstone.png"));
            this.model.renderOnly("group_Redstone");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MOD_ID, "textures/models/hourglass-sand.png"));
        if (renderingItem) {
            this.model.renderOnly("sandbtm0");
        } else {
            long time = System.nanoTime();
            float var10000 = (float)(time - te.timeDidFlip) / 1.0E9F;
            float rotation = var10000 / 0.25F;
            if ((double)rotation < 1.0D) {
                GL11.glRotated(180.0D, 1.0D, 0.0D, 0.0D);
                GL11.glRotated(180.0D * (double)rotation, 0.0D, 0.0D, 1.0D);
            }

            this.renderSandState(te, (double)rotation < 1.0D);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MOD_ID, "textures/models/hourglass-glass.png"));
        this.model.renderOnly("Group_glass");
        GL11.glPopMatrix();
    }

    private void renderSandState(TileHourglass te, boolean rotating) {
        float percent = te.percentageComplete();
        float sectionSize = 16.666666F;
        if (percent <= sectionSize) {
            this.model.renderOnly("sandtop1");
            if (!rotating) {
                this.model.renderOnly("sandbtm6");
            }
        } else if (percent <= sectionSize * 2.0F) {
            this.model.renderOnly("sandtop2");
            this.model.renderOnly("sandbtm5");
        } else if (percent <= sectionSize * 3.0F) {
            this.model.renderOnly("sandtop3");
            this.model.renderOnly("sandbtm4");
        } else if (percent <= sectionSize * 4.0F) {
            this.model.renderOnly("sandtop4");
            this.model.renderOnly("sandbtm3");
        } else if (percent <= sectionSize * 5.0F) {
            this.model.renderOnly("sandtop5");
            this.model.renderOnly("sandbtm2");
        } else if (percent < 100.0F) {
            this.model.renderOnly("sandtop6");
            this.model.renderOnly("sandbtm1");
        } else {
            this.model.renderOnly("sandbtm0");
        }

    }
}
