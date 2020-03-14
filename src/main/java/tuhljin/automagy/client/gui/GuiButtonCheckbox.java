package tuhljin.automagy.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class GuiButtonCheckbox extends GuiButton {
    public boolean checked = false;
    protected ResourceLocation texture;
    protected int texX;
    protected int texY;

    public GuiButtonCheckbox(int id, int x, int y, boolean checked, ResourceLocation texture, int texX, int texY) {
        super(id, x, y, 8, 8, "checkbox");
        this.checked = checked;
        this.texture = texture;
        this.texX = texX;
        this.texY = texY;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int x, int y, float delta) {
        if (this.visible) {
            int tY = this.checked ? this.texY + 8 : this.texY;
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(770, 771);
            this.drawTexturedModalRect(this.x, this.y, this.texX, tY, 8, 8);
            this.mouseDragged(mc, x, y);
        }

    }
}