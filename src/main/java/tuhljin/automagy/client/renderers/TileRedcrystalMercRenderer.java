package tuhljin.automagy.client.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.tiles.TileRedcrystalMerc;

public class TileRedcrystalMercRenderer extends TileEntitySpecialRenderer<TileRedcrystalMerc> {
    private final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(References.MOD_ID, "models/obj/merccrystal.obj"));
    public static final String OBJ_MODEL_MERC = "models/obj/merccrystal.obj";
    public static final String TEXTURE_MERC = "textures/models/merccrystal.png";
    public static final String TEXTURE_MERC_ACTIVE = "textures/models/merccrystalActive.png";

    public TileRedcrystalMercRenderer() {
    }

    public void render(TileRedcrystalMerc te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        boolean renderingItem = ItemBlockSpecialRenderer.isRendering;
        boolean active = !renderingItem && te.getBlockMetadata() != 0;
        String texture = active ? "textures/models/merccrystalActive.png" : "textures/models/merccrystal.png";
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(x + 0.5D, y, z + 0.5D);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        if (renderingItem) {
            GL11.glTranslated(0.0D, 0.3D, 0.0D);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(References.MOD_ID, texture));
        if (renderingItem) {
            this.model.renderAll();
        } else {
            if (te.connectN) {
                this.model.renderPart("adjunctA_PlaneA");
            }

            if (te.connectS) {
                this.model.renderPart("adjunctC_PlaneC");
            }

            if (te.connectE) {
                this.model.renderPart("adjunctB_PlaneB");
            }

            if (te.connectW) {
                this.model.renderPart("adjunctD_PlaneD");
            }

            this.model.renderPart("Slab");
            float rotationAngle = 1.0F + (float)te.clientRenderRotationHelper / (float)TileRedcrystalMerc.rotationSpeedFactor * 360.0F;
            GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(0.0D, te.clientRenderFloatingDistance, 0.0D);
            this.model.renderPart("CrystalsMain");
        }

        GL11.glPopMatrix();
    }
}
