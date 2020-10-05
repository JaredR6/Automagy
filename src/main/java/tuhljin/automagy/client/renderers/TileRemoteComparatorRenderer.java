package tuhljin.automagy.client.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;
import tuhljin.automagy.common.tiles.TileRemoteComparator;

public class TileRemoteComparatorRenderer extends TileEntitySpecialRenderer<TileRemoteComparator> {
    private final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("automagy".toLowerCase(), "models/obj/comparator.obj"));
    public static final String OBJ_MODEL = "models/obj/comparator.obj";
    public static final String TEXTURE_STANDARD = "textures/models/comparator.png";
    public static final String TEXTURE_READING = "textures/models/comparatorReading.png";
    public static final String TEXTURE_SIGNALING = "textures/models/comparatorSignaling.png";

    public TileRemoteComparatorRenderer() {
    }

    public void renderTileEntityAt(TileRemoteComparator te, double x, double y, double z, float partialTicks, int destroyStage) {
        boolean renderingItem = ItemBlockSpecialRenderer.isRendering;
        ItemStack stack = renderingItem ? null : te.getFloatingDisplayItem();
        String texture;
        if (stack == null) {
            texture = "textures/models/comparator.png";
        } else if (te.isRedstoneSignalBeingSent()) {
            texture = "textures/models/comparatorSignaling.png";
        } else {
            texture = "textures/models/comparatorReading.png";
        }

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        if (renderingItem) {
            GL11.glTranslated(x + 0.5D, y + 0.25D, z + 0.5D);
            GL11.glScaled(0.5D, 0.5D, 0.5D);
        } else {
            GL11.glTranslated(x + 0.5D, y, z + 0.5D);
            GL11.glScaled(0.5D, 0.5D, 0.5D);
        }

        EnumFacing facing = renderingItem ? EnumFacing.NORTH : te.getFacing();
        switch(facing) {
            case NORTH:
            default:
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            case WEST:
                break;
            case EAST:
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("automagy".toLowerCase(), texture));
        this.model.renderAll();
        GL11.glPopMatrix();
        if (stack != null) {
            this.renderFloatingItem(te.getWorld(), stack, x, y, z, destroyStage);
        }

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
}
