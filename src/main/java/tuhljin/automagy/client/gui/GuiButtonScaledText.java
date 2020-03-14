package tuhljin.automagy.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;

public class GuiButtonScaledText extends GuiButton {
    protected static final ResourceLocation buttonTexturesRecolor = new ResourceLocation(References.GUI_WIDGETS_RECOLOR);
    public boolean texturesRecolored;
    private float scaleFactor;
    private boolean mouseIsHoveringOver;

    public GuiButtonScaledText(int id, int x, int y, int width, int height, String text, float scaleFactor, boolean recolorTextures) {
        super(id, x, y, width, height, text);
        this.texturesRecolored = false;
        this.mouseIsHoveringOver = false;
        this.scaleFactor = scaleFactor;
        this.texturesRecolored = recolorTextures;
    }

    public GuiButtonScaledText(int id, int x, int y, int width, int height, String text, float scaleFactor) {
        this(id, x, y, width, height, text, scaleFactor, false);
    }

    public GuiButtonScaledText(int id, int x, int y, String text, float scaleFactor, boolean recolorTextures) {
        this(id, x, y, 200, 20, text, scaleFactor, recolorTextures);
    }

    public GuiButtonScaledText(int id, int x, int y, String text, float scaleFactor) {
        this(id, x, y, 200, 20, text, scaleFactor, false);
    }

    @Override
    public void drawCenteredString(FontRenderer fontRenderer, @Nonnull String text, int x, int y, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(this.scaleFactor, this.scaleFactor, this.scaleFactor);
        super.drawCenteredString(fontRenderer, text, (int)((float)x / this.scaleFactor), (int)((float)y / this.scaleFactor), color);
        GL11.glPopMatrix();
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(this.texturesRecolored ? buttonTexturesRecolor : BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x, this.y + this.height / 2, 0, 66 + i * 20 - this.height / 2, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y + this.height / 2, 200 - this.width / 2, 66 + i * 20 - this.height / 2, this.width / 2, this.height / 2);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 0xE0E0E0;
            if (this.packedFGColour != 0) {
                j = this.packedFGColour;
            } else if (!this.enabled) {
                    j = 0xA0A0A0;
            } else if (this.hovered) {
                j = 0xFFFFA0;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, (int)((float)this.y + ((float)this.height - 8.0F * this.scaleFactor) / 2.0F), j);
            this.mouseIsHoveringOver = i == 2;
        }

    }

    public boolean isMouseHoveringOver() {
        return this.mouseIsHoveringOver;
    }
}
