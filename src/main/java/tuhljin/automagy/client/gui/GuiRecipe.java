package tuhljin.automagy.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tuhljin.automagy.common.gui.ContainerRecipe;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;

public class GuiRecipe extends ModGuiContainer {
    @Nonnull
    public final ResourceLocation texture;
    private ContainerRecipe container;
    private GuiTextFieldWithReporting textFieldNameFilter;
    private String prevText;

    private GuiRecipe(ContainerRecipe container, EntityPlayer player) {
        super(container);
        this.texture = new ResourceLocation(References.GUI_RUNICRECIPE);
        this.prevText = "";
        this.container = container;
        this.ySize = 180;
    }

    public GuiRecipe(@Nonnull EntityPlayer player) {
        this(new ContainerRecipe(player), player);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 7 + this.container.blockedSlotID * 18, this.guiTop + 155, 0, this.ySize, this.container.blockedSlotID == 8 ? 18 : 17, 18);
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        super.drawScreen(x, y, partialTicks);
    }
}
