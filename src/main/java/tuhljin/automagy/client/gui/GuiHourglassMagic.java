package tuhljin.automagy.client.gui;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.gui.ContainerHourglassMagic;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileHourglassMagic;

import javax.annotation.Nonnull;

public class GuiHourglassMagic extends ModGuiContainerAttachable<TileHourglassMagic> {
    @Nonnull
    public final ResourceLocation texture;
    public static final String[] strSignal = {I18n.format("automagy.gui.hourglassMagic.signal.0"), I18n.format("automagy.gui.hourglassMagic.signal.1"), I18n.format("automagy.gui.hourglassMagic.signal.2"), I18n.format("automagy.gui.hourglassMagic.signal.3"), I18n.format("automagy.gui.hourglassMagic.signal.4")};
    public static final String[] strSandFlip = {I18n.format("automagy.gui.hourglassMagic.sandFlip.0"), I18n.format("automagy.gui.hourglassMagic.sandFlip.1"), I18n.format("automagy.gui.hourglassMagic.sandFlip.2")};
    public static final String[] strSignalReaction = {I18n.format("automagy.gui.hourglassMagic.signalReact.0"), I18n.format("automagy.gui.hourglassMagic.signalReact.1"), I18n.format("automagy.gui.hourglassMagic.signalReact.2"), I18n.format("automagy.gui.hourglassMagic.signalReact.3")};

    public GuiHourglassMagic(TileHourglassMagic tile, InventoryPlayer invPlayer) {
        super(new ContainerHourglassMagic(tile, invPlayer));
        this.texture = new ResourceLocation(References.GUI_CHECKBOXABLE);
    }

    @Override
    public void initGui() {
        super.initGui();
        int height = (int)(20.0F * this.scaleFactor);
        this.buttonList.add(new GuiButtonScaledText(1, this.guiLeft + 38, this.guiTop + 12, 40, 12, I18n.format("automagy.gui.hourglassMagic.timer.min"), this.scaleFactor * 0.6666667F));
        this.buttonList.add(new GuiButtonScaledText(2, this.guiLeft + 80, this.guiTop + 12, 40, 12, I18n.format("automagy.gui.hourglassMagic.timer.sec"), this.scaleFactor * 0.6666667F));
        this.buttonList.add(new GuiButtonScaledText(3, this.guiLeft + 75, this.guiTop + 31, 95, height, strSignal[1], this.scaleFactor));
        this.buttonList.add(new GuiButtonScaledText(4, this.guiLeft + 75, this.guiTop + 35 + height, 95, height, strSandFlip[0], this.scaleFactor));
        this.buttonList.add(new GuiButtonCheckbox(6, this.guiLeft + 145, this.guiTop + 10, false, this.texture, this.xSize, 0));
        this.buttonList.add(new GuiButtonScaledText(5, this.guiLeft + 75, this.guiTop + 39 + height * 2, 95, height, strSignalReaction[0], this.scaleFactor));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + 20, 0, 0, this.xSize, this.ySize);
        GL11.glPushMatrix();
        GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
        this.drawStringScaled(this.fontRenderer, I18n.format("automagy.gui.hourglassMagic.timer.name"), (float)(this.guiLeft + 2), (float)(this.guiTop + 1), 0xFFFFFF);
        this.drawStringScaled(this.fontRenderer, I18n.format("automagy.gui.hourglassMagic.signal.name"), (float)(this.guiLeft + 2), (float)(this.guiTop + 35), 0xFFFFFF);
        this.drawStringScaled(this.fontRenderer, I18n.format("automagy.gui.hourglassMagic.sandFlip.name"), (float)(this.guiLeft + 2), (float)(this.guiTop + 55), 0xFFFFFF);
        this.drawStringScaled(this.fontRenderer, I18n.format("automagy.gui.hourglassMagic.signalReact.name"), (float)(this.guiLeft + 2), (float)(this.guiTop + 75), 0xFFFFFF);
        this.drawCenteredStringScaled(this.fontRenderer, I18n.format("automagy.gui.hourglassMagic.repeat"), this.guiLeft + 149, this.guiTop + 1, 0xFFFFFF);
        GL11.glPopMatrix();
        int time = this.tile.getTargetTimeSeconds();
        if (time >= 60) {
            int min = time / 60;
            int sec = time % 60;
            this.drawString(this.fontRenderer, I18n.format("automagy.gui.hourglassMagic.timer.minutesAndSeconds", min, sec), this.guiLeft + 40, this.guiTop, 0xFFFFFF);
        } else if (time == 1) {
            this.drawString(this.fontRenderer, I18n.format("automagy.gui.hourglassMagic.timer.secondsOne"), this.guiLeft + 40, this.guiTop, 0xFFFFFF);
        } else {
            this.drawString(this.fontRenderer, I18n.format("automagy.gui.hourglassMagic.timer.seconds", time), this.guiLeft + 40, this.guiTop, 0xFFFFFF);
        }

    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        super.drawScreen(x, y, partialTicks);
        if (((GuiButtonScaledText)this.buttonList.get(0)).isMouseHoveringOver() || ((GuiButtonScaledText)this.buttonList.get(1)).isMouseHoveringOver()) {
            ArrayList<String> lines = TjUtil.getMultiLineLocalizedString("automagy.gui.hourglassMagic.timer.tooltip");
            GL11.glPushMatrix();
            GL11.glTranslatef((float)this.guiLeft, (float)this.guiTop, 0.0F);
            GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
            this.drawScaledHoveringText(lines, x - this.guiLeft - 10, y - this.guiTop + 20, this.fontRenderer);
            GL11.glPopMatrix();
        }

    }

    @Override
    protected void buttonClicked(@Nonnull GuiButton button, boolean rightClick) {
        int amount;
        if (button.id != 1 && button.id != 2) {
            amount = button.id;
            if (rightClick) {
                amount *= -1;
            }

            switch(button.id) {
                case 3:
                case 4:
                case 5:
                    this.sendPacket(amount);
                    break;
                case 6:
                    this.sendPacket(this.tile.modeRepeat ? -6 : 6);
            }

        } else {
            amount = button.id == 1 ? 60 : 1;
            if (isShiftKeyDown()) {
                amount *= 10;
            }

            amount += 100;
            if (rightClick) {
                amount *= -1;
            }

            this.sendPacket(amount);
        }
    }

    @Override
    public void receiveContainerUpdate(int id, int data) {
        GuiButton button = null;

        for (GuiButton b : this.buttonList) {
            if (b.id == id) {
                button = b;
                break;
            }
        }

        if (button == null) {
            Automagy.logWarning("GUIHourglassMagic received an invalid update. Bad button ID. Ignoring. (id=" + id + ")");
        } else if (button instanceof GuiButtonCheckbox) {
            ((GuiButtonCheckbox)button).checked = data == 1;
        } else {
            String[] strArr;
            switch(button.id) {
                case 3:
                    strArr = strSignal;
                    break;
                case 4:
                    strArr = strSandFlip;
                    break;
                case 5:
                    strArr = strSignalReaction;
                    break;
                default:
                    Automagy.logWarning("GUIHourglassMagic received an invalid update. Ignoring. (id=" + id + ")");
                    return;
            }

            if (data >= 0 && data < strArr.length) {
                button.displayString = strArr[data];
            } else {
                Automagy.logWarning("GUIHourglassMagic received an invalid update. Ignoring. (id=" + id + ", data=" + data + ")");
            }
        }
    }
}
