package tuhljin.automagy.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import tuhljin.automagy.common.gui.ContainerMaw;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileMawBase;
import tuhljin.automagy.common.tiles.TileMawSpitting;

public class GuiMaw extends GuiWithSingleFilter<TileMawBase> {
    protected List<String> textWhitelistItemCount;
    protected List<String> textBlacklistItemCount;
    protected GuiButtonCheckbox checkboxRedSense;

    public GuiMaw(TileMawBase te, InventoryPlayer invPlayer) {
        super(new ContainerMaw(te, invPlayer));
        String s = te instanceof TileMawSpitting ? "spittingMaw" : "finicalMaw";
        this.textWhitelistItemCount = TjUtil.getMultiLineLocalizedString("Automagy.gui." + s + ".quantityResult.limit");
        this.textBlacklistItemCount = TjUtil.getMultiLineLocalizedString("Automagy.gui." + s + ".quantityResult.leave");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.checkboxRedSense = new GuiButtonCheckbox(0, this.guiLeft + 104, this.guiTop + 32, this.tile.isRedstoneSensitive(), this.texture, this.xSize, 0);
        this.buttonList.add(this.checkboxRedSense);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        GL11.glPushMatrix();
        GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
        this.drawStringScaled(this.fontRenderer, I18n.format("Automagy.gui.maw.redstoneDisables"), this.guiLeft + 117, this.guiTop + 34, 0xFFFFFF);
        GL11.glPopMatrix();
        if (this.listInstalled && this.filterUseItemCount) {
            float old = this.scaleFactor;
            this.scaleFactor *= 0.7F;
            GL11.glPushMatrix();
            GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
            String s = "Automagy.gui.finicalMaw.quantityResult.";
            // TODO: What is this line for?
            // (new StringBuilder()).append(s).append(this.filterIsBlacklist ? "leave" : "limit").toString();
            int x = this.guiLeft + 88;
            int y = this.guiTop + 51;
            this.drawCenteredStringScaledMultiLine(this.fontRenderer, this.filterIsBlacklist ? this.textBlacklistItemCount : this.textWhitelistItemCount, x, y, 0xFFFFFF);
            GL11.glPopMatrix();
            this.scaleFactor = old;
        }

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void buttonClicked(GuiButton button, boolean rightClick) {
        if (button == this.checkboxRedSense) {
            GuiButtonCheckbox checkbox = (GuiButtonCheckbox)button;
            checkbox.checked = !checkbox.checked;
            this.sendPacket(checkbox.checked ? 1 : 0);
        }

    }

    @Override
    public void receiveContainerUpdate(int id, int data) {
        if (id == 0) {
            this.checkboxRedSense.checked = data == 1;
        }

    }
}
