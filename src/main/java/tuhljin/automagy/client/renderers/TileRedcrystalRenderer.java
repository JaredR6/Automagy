package tuhljin.automagy.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalAmp;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalDense;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalDim;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalRes;
import tuhljin.automagy.common.tiles.TileRedcrystal;

public class TileRedcrystalRenderer extends TileEntitySpecialRenderer {
    private final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("automagy".toLowerCase(), "models/obj/redcrystal.obj"));
    public static final String OBJ_MODEL = "models/obj/redcrystal.obj";
    public static final String TEXTURE_STANDARD = "textures/models/redcrystalStandard.png";
    public static final String TEXTURE_ACTIVE = "textures/models/redcrystalActive.png";
    public static final String TEXTURE_AMP = "textures/models/redcrystalAmp.png";
    public static final String TEXTURE_AMP_ACTIVE = "textures/models/redcrystalAmpActive.png";
    public static final String TEXTURE_DIM = "textures/models/redcrystalDim.png";
    public static final String TEXTURE_DIM_ACTIVE = "textures/models/redcrystalDimActive.png";
    public static final String TEXTURE_DENSE = "textures/models/redcrystalDense.png";
    public static final String TEXTURE_DENSE_ACTIVE = "textures/models/redcrystalDenseActive.png";
    public static final String TEXTURE_RES = "textures/models/redcrystalRes.png";
    public static final String TEXTURE_RES_ACTIVE = "textures/models/redcrystalResActive.png";

    public TileRedcrystalRenderer() {
    }

    private void adjustRotateViaMeta(World world, int x, int y, int z) {
        int meta = 0;
        GL11.glPushMatrix();
        GL11.glRotatef((float)(meta * -90), 0.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public void func_180535_a(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        TileRedcrystal ter = (TileRedcrystal)te;
        boolean renderingItem = ItemBlockSpecialRenderer.isRendering;
        EnumFacing orientation = ter.orientation;
        Block block = null;
        if (orientation != null && !renderingItem) {
            block = ter.getBlockType();
        } else {
            renderingItem = true;
            orientation = EnumFacing.UP;
            block = ItemBlockSpecialRenderer.currentBlock;
        }

        boolean larger = false;
        boolean powered = !renderingItem && te.getBlockMetadata() > 0;
        String texture;
        if (block instanceof BlockRedcrystalAmp) {
            texture = powered ? "textures/models/redcrystalAmpActive.png" : "textures/models/redcrystalAmp.png";
            larger = true;
        } else if (block instanceof BlockRedcrystalDim) {
            texture = powered ? "textures/models/redcrystalDimActive.png" : "textures/models/redcrystalDim.png";
            larger = true;
        } else if (block instanceof BlockRedcrystalDense) {
            texture = powered ? "textures/models/redcrystalDenseActive.png" : "textures/models/redcrystalDense.png";
            larger = true;
        } else if (block instanceof BlockRedcrystalRes) {
            texture = powered ? "textures/models/redcrystalResActive.png" : "textures/models/redcrystalRes.png";
            larger = true;
        } else {
            texture = powered ? "textures/models/redcrystalActive.png" : "textures/models/redcrystalStandard.png";
        }

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(x + 0.5D, y, z + 0.5D);
        this.translateFromOrientation((float)x, (float)y, (float)z, orientation);
        if (renderingItem) {
            if (!larger) {
                GL11.glScaled(0.9D, 0.9D, 0.9D);
            }

            GL11.glRotated(170.0D, 0.0D, 1.0D, 0.0D);
        } else if (larger) {
            GL11.glScaled(0.4D, 0.4D, 0.4D);
        } else {
            GL11.glScaled(0.25D, 0.25D, 0.25D);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("automagy".toLowerCase(), texture));
        this.model.renderPart("primary_Plane001");
        if (orientation == EnumFacing.DOWN) {
            if (ter.connectN) {
                this.model.renderPart("adjunctC_PlaneC");
            }

            if (ter.connectS) {
                this.model.renderPart("adjunctA_PlaneA");
            }
        } else {
            if (ter.connectN) {
                this.model.renderPart("adjunctA_PlaneA");
            }

            if (ter.connectS) {
                this.model.renderPart("adjunctC_PlaneC");
            }
        }

        if (ter.connectE) {
            this.model.renderPart("adjunctB_PlaneB");
        }

        if (ter.connectW) {
            this.model.renderPart("adjunctD_PlaneD");
        }

        GL11.glPopMatrix();
    }

    private void translateFromOrientation(float x, float y, float z, EnumFacing orientation) {
        switch(orientation) {
            case DOWN:
                GL11.glTranslatef(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            case UP:
            default:
                break;
            case NORTH:
                GL11.glTranslatef(0.0F, 0.5F, 0.5F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glTranslatef(0.0F, 0.5F, -0.5F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case WEST:
                GL11.glTranslatef(0.5F, 0.5F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                break;
            case EAST:
                GL11.glTranslatef(-0.5F, 0.5F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
        }

    }
}
