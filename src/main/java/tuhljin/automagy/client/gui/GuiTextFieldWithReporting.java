package tuhljin.automagy.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;

public class GuiTextFieldWithReporting extends GuiTextField {
    public ModGuiContainer parent;
    @Nullable
    public String emptyMessage = null;
    private FontRenderer font;

    public GuiTextFieldWithReporting(int id, ModGuiContainer parent, @Nonnull FontRenderer fontRenderer, int xPos, int yPos, int width, int height) {
        super(id, fontRenderer, xPos, yPos, width, height);
        this.parent = parent;
        this.font = fontRenderer;
    }

    @Override
    public void setFocused(boolean flag) {
        boolean wasFocused = this.isFocused();
        super.setFocused(flag);
        if (!flag && wasFocused) {
            this.parent.onTextFieldLostFocus(this);
        }

    }

    @Override
    public boolean textboxKeyTyped(char c, int cI) {
        if (this.isFocused() && cI == 28) {
            this.setFocused(false);
            return true;
        } else {
            return super.textboxKeyTyped(c, cI);
        }
    }

    public boolean pointIsOver(int x, int y) {
        if (this.parent != null) {
            x = (int)(x / this.parent.scaleFactorTextField);
            y = (int)(y / this.parent.scaleFactorTextField);
        }

        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public void setEmptyMessage(@Nullable String str) {
        if (str != null && !str.isEmpty()) {
            this.emptyMessage = TextFormatting.GRAY + "" + TextFormatting.ITALIC + str;
        } else {
            this.emptyMessage = null;
        }

    }

    @Override
    public void drawTextBox() {
        super.drawTextBox();
        if (this.emptyMessage != null && this.getVisible() && this.getText().isEmpty() && !this.isFocused()) {
            int gray = 0xE0E0E0;
            int x = this.getEnableBackgroundDrawing() ? this.x + 4 : this.x;
            int y = this.getEnableBackgroundDrawing() ? this.y + (this.height - 8) / 2 : this.y;
            this.font.drawStringWithShadow(this.emptyMessage, x, y, gray);
        }

    }
}
