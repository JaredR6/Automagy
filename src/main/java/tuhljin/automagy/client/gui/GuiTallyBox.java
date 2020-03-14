package tuhljin.automagy.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import tuhljin.automagy.common.gui.ContainerTallyBox;
import tuhljin.automagy.common.tiles.TileTallyBase;

public class GuiTallyBox extends GuiWithSingleFilter<TileTallyBase> {
    protected GuiButtonCheckbox checkboxMatchAll;

    public GuiTallyBox(TileTallyBase te, InventoryPlayer invPlayer) {
        super(new ContainerTallyBox(te, invPlayer));
    }

    @Override
    public void initGui() {
        super.initGui();
        this.checkboxMatchAll = new GuiButtonCheckbox(0, this.guiLeft + 104, this.guiTop + 32, !this.tile.requireAllMatches, this.texture, this.xSize, 0);
        this.buttonList.add(this.checkboxMatchAll);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        GL11.glPushMatrix();
        GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
        this.drawStringScaled(this.fontRenderer, I18n.format("Automagy.gui.tallyBox.allowPartialMatch"), (float)(this.guiLeft + 117), (float)(this.guiTop + 34), 0xFFFFFF);
        GL11.glPopMatrix();
        if (this.listInstalled) {
            float old = this.scaleFactor;
            this.scaleFactor *= 0.7F;
            GL11.glPushMatrix();
            GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
            int x = this.guiLeft + 88;
            int y = this.guiTop + 51;
            this.drawCenteredStringScaled(this.fontRenderer, I18n.format("Automagy.gui.tallyBox.desc." + (this.filterIsBlacklist ? "black" : "white")), x, y, 0xFFFFFF);
            this.drawCenteredStringScaled(this.fontRenderer, I18n.format("Automagy.gui.tallyBox.desc." + (this.checkboxMatchAll.checked ? "any" : "all")), x, y + 6, 0xFFFFFF);
            if (this.filterUseItemCount) {
                this.drawCenteredStringScaled(this.fontRenderer, I18n.format("Automagy.gui.tallyBox.quantityResult." + (this.filterIsBlacklist ? "black" : "white")), x, y + 15, 0xFFFFFF);
            }

            GL11.glPopMatrix();
            this.scaleFactor = old;
        }

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    protected void buttonClicked(GuiButton button, boolean rightClick) {
        if (button == this.checkboxMatchAll) {
            GuiButtonCheckbox checkbox = (GuiButtonCheckbox)button;
            checkbox.checked = !checkbox.checked;
            this.sendPacket(checkbox.checked ? 1 : 0);
        }

    }

    public void receiveContainerUpdate(int id, int data) {
        if (id == 0) {
            this.checkboxMatchAll.checked = data != 1;
        }

    }
}