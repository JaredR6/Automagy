package tuhljin.automagy.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import tuhljin.automagy.common.gui.ContainerGolemWorkbench;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.tiles.TileGolemWorkbench;

public class GuiGolemWorkbench extends ModGuiContainer {
    public final ResourceLocation texture;

    public GuiGolemWorkbench(TileGolemWorkbench te, InventoryPlayer invPlayer) {
        super(new ContainerGolemWorkbench(te, invPlayer));
        this.texture = new ResourceLocation(References.GUI_GOLEMWORKBENCH);
        this.ySize = 205;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
