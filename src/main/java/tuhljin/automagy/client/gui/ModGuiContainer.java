package tuhljin.automagy.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public abstract class ModGuiContainer extends GuiContainer {
    protected float scaleFactor = 0.75F;
    protected float scaleFactorTextField = 0.75F;
    protected List<GuiTextField> textFieldList = new ArrayList<>();

    public ModGuiContainer(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    void sendPacket(int id) {
        this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, id);
    }

    public void drawStringScaled(FontRenderer renderer, String str, float x, float y, int color) {
        this.drawString(renderer, str, (int)(x / this.scaleFactor), (int)(y / this.scaleFactor), color);
    }

    public void drawStringScaledNoShadow(FontRenderer renderer, String text, float x, float y, int color) {
        renderer.drawString(text, (int)(x / this.scaleFactor), (int)(y / this.scaleFactor), color);
    }

    public void drawCenteredStringScaled(FontRenderer renderer, String text, int x, int y, int color) {
        this.drawCenteredString(renderer, text, (int)(x / this.scaleFactor), (int)(y / this.scaleFactor), color);
    }

    public void drawCenteredStringScaledMultiLine(FontRenderer renderer, List<String> lines, int x, int y, int color) {
        for (String line : lines) {
            this.drawCenteredStringScaled(renderer, line, x, y, color);
            y += 6;
        }

    }

    public void drawCenteredStringScaledNoShadow(FontRenderer renderer, String text, int x, int y, int color) {
        x = (int)(x / this.scaleFactor);
        y = (int)(y / this.scaleFactor);
        renderer.drawString(text, x - renderer.getStringWidth(text) / 2, y, color);
    }

    protected void drawScaledHoveringText(List<String> list, int x, int y, FontRenderer font) {
        this.drawHoveringText(list, (int)(x / this.scaleFactor), (int)(y / this.scaleFactor), font);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        this.buttonClicked(button, false);
    }

    protected void buttonClicked(GuiButton button, boolean rightClick) {
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int scaleAdjusted1;
        if (mouseButton == 1) {
            for(scaleAdjusted1 = 0; scaleAdjusted1 < this.buttonList.size(); ++scaleAdjusted1) {
                GuiButton guibutton = this.buttonList.get(scaleAdjusted1);
                if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.buttonClicked(guibutton, true);
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.textFieldList.size() > 0) {
            scaleAdjusted1 = (int)(mouseX / this.scaleFactorTextField);
            int scaleAdjusted2 = (int)(mouseY / this.scaleFactorTextField);

            for (GuiTextField input : this.textFieldList) {
                if (input.getVisible()) {
                    input.mouseClicked(scaleAdjusted1, scaleAdjusted2, mouseButton);
                    if (mouseButton == 1 && input.isFocused()) {
                        input.setText("");
                    }
                }
            }
        }

    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        for (GuiTextField input : this.textFieldList) {
            input.updateCursorCounter();
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (this.textFieldList.size() > 0) {
            GL11.glPushMatrix();
            GL11.glScalef(this.scaleFactorTextField, this.scaleFactorTextField, this.scaleFactorTextField);
            GL11.glDisable(2896);
            GL11.glDisable(3042);

            for (GuiTextField input : this.textFieldList) {
                input.drawTextBox();
            }

            GL11.glPopMatrix();
        }

    }

    @Override
    protected void keyTyped(char c, int cI) throws IOException {
        boolean actionKey;
        boolean controlKey;
        if (isCtrlKeyDown()) {
            actionKey = true;
            controlKey = true;
        } else {
            actionKey = cI == 14 || cI == 211 || cI == 203 || cI == 205 || cI == 199 || cI == 207 || cI == 28;
            controlKey = false;
        }


        String text = "";
        GuiTextField input = null;

        for (GuiTextField field : this.textFieldList) {
            if (actionKey || this.textFieldAllowsCharacter(field, c, field.getText()) ) {
                if (field.textboxKeyTyped(c, cI)) {
                    text = field.getText();
                    input = field;
                    break;
                }
            }
        }
        if (input == null) {
            super.keyTyped(c, cI);
            return;
        }

        /*
        Iterator var5 = this.textFieldList.iterator();
        do {
            do {
                if (!var5.hasNext()) {
                    super.keyTyped(c, cI);
                    return;
                }

                input = (GuiTextField)var5.next();
            } while(!actionKey && !this.textFieldAllowsCharacter(input, c, input.getText()));

            text = input.getText();
        } while(!input.textboxKeyTyped(c, cI));
         */

        String newText;
        if (controlKey) {
            String testText = input.getText();
            if (!testText.equals(text)) {
                StringBuilder sb = new StringBuilder();

                for(int i = 0; i < testText.length(); ++i) {
                    char c2 = testText.charAt(i);
                    if (this.textFieldAllowsCharacter(input, c2, sb.toString())) {
                        sb.append(c2);
                    }
                }

                newText = sb.toString();
                if (!testText.equals(newText)) {
                    input.setText(newText);
                }
            } else {
                newText = testText;
            }
        } else {
            newText = input.getText();
        }

        if (!text.equals(newText)) {
            this.onTextFieldChanged(input, newText);
        }

    }

    protected void onTextFieldChanged(GuiTextField textField, String newText) {
    }

    protected boolean textFieldAllowsCharacter(GuiTextField textField, char c, String currentText) {
        return true;
    }

    public void onTextFieldLostFocus(GuiTextFieldWithReporting textField) {
    }
}
