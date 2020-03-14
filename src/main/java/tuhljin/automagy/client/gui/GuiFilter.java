package tuhljin.automagy.client.gui;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tuhljin.automagy.common.gui.ContainerFilter;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.network.MessageGUIFilter;

public class GuiFilter extends ModGuiContainer {
    public final boolean blacklist;
    public final ResourceLocation texture;
    private ContainerFilter container;
    private GuiTextFieldWithReporting textFieldNameFilter;
    private String prevText;

    private GuiFilter(ContainerFilter container, boolean isBlacklist, EntityPlayer player) {
        super(container);
        this.prevText = "";
        this.container = container;
        this.blacklist = isBlacklist;
        this.texture = new ResourceLocation(this.blacklist ? References.GUI_RUNICBLACKLIST : References.GUI_RUNICWHITELIST);
        this.ySize = 180;
    }

    public GuiFilter(boolean isBlacklist, EntityPlayer player) {
        this(new ContainerFilter(isBlacklist, player), isBlacklist, player);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButtonCheckbox(0, this.guiLeft + 80, this.guiTop + 18, !this.container.filterInventory.useItemCount, this.texture, this.xSize, 0));
        this.buttonList.add(new GuiButtonCheckbox(1, this.guiLeft + 80, this.guiTop + 28, this.container.filterInventory.ignoreMetadata, this.texture, this.xSize, 0));
        this.buttonList.add(new GuiButtonCheckbox(2, this.guiLeft + 80, this.guiTop + 38, this.container.filterInventory.ignoreNBT, this.texture, this.xSize, 0));
        this.prevText = this.container.filterInventory.getNameFilter();
        if (this.textFieldNameFilter == null) {
            this.textFieldNameFilter = new GuiTextFieldWithReporting(3, this, this.fontRenderer, (int)((float)(this.guiLeft + 81) / this.scaleFactorTextField), (int)((float)(this.guiTop + 59) / this.scaleFactorTextField), 100, 16);
            this.textFieldNameFilter.setText(this.prevText);
            this.textFieldList.add(this.textFieldNameFilter);
        } else {
            this.textFieldNameFilter.x = (int)((this.guiLeft + 81) / this.scaleFactorTextField);
            this.textFieldNameFilter.y = (int)((this.guiTop + 59) / this.scaleFactorTextField);
        }

    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 7 + this.container.blockedSlotID * 18, this.guiTop + 155, 0, this.ySize, this.container.blockedSlotID == 8 ? 18 : 17, 18);
        GL11.glPushMatrix();
        GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
        int color = this.blacklist ? 0xFFFFFF : 0;
        this.drawStringScaledNoShadow(this.fontRenderer, I18n.format("Automagy.gui.filter.ignoreQuantity"), this.guiLeft + 91,this.guiTop + 20, color);
        this.drawStringScaledNoShadow(this.fontRenderer, I18n.format("Automagy.gui.filter.ignoreMetadata"), this.guiLeft + 91, this.guiTop + 29.5F, color);
        this.drawStringScaledNoShadow(this.fontRenderer, I18n.format("Automagy.gui.filter.ignoreNBT"), this.guiLeft + 91, this.guiTop + 39, color);
        this.drawStringScaledNoShadow(this.fontRenderer, I18n.format("Automagy.gui.filter.byName"), this.guiLeft + 81, this.guiTop + 50.5F, color);
        this.drawStringScaledNoShadow(this.fontRenderer, "[?]", this.guiLeft + 145, this.guiTop + 50.5F, this.blacklist ? '쳿' : 0x0066FF);
        GL11.glPopMatrix();
        super.drawGuiContainerBackgroundLayer(var1, var2, var3);
    }

    @Override
    public void drawScreen(int x, int y, float p_73863_3_) {
        super.drawScreen(x, y, p_73863_3_);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)this.guiLeft, (float)this.guiTop, 0.0F);
        if (x >= this.guiLeft + 145 && x < this.guiLeft + 155 && y >= this.guiTop + 50 && y < this.guiTop + 56) {
            ArrayList<String> lines = TjUtil.getMultiLineLocalizedString("Automagy.gui.filter.textFilterTooltip");
            GL11.glPushMatrix();
            GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
            this.drawScaledHoveringText(lines, x - this.guiLeft - 20, y - this.guiTop + 20, this.fontRenderer);
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }

    protected void buttonClicked(GuiButton button, boolean rightClick) {
        if (button instanceof GuiButtonCheckbox) {
            GuiButtonCheckbox checkbox = (GuiButtonCheckbox)button;
            checkbox.checked = !checkbox.checked;
            MessageGUIFilter.sendToServer(button.id, checkbox.checked ? "1" : "0");
        }

    }

    public void onTextFieldLostFocus(GuiTextFieldWithReporting textField) {
        String text = textField.getText();
        if (!text.equals(this.prevText)) {
            this.prevText = text;
            MessageGUIFilter.sendToServer(textField.getId(), text);
        }

    }
}
