package dev.heliosclient.ui.clickgui;

import dev.heliosclient.HeliosClient;
import dev.heliosclient.managers.ColorManager;
import dev.heliosclient.managers.FontManager;
import dev.heliosclient.util.Renderer2D;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;

public class Tooltip {
    public static final Tooltip tooltip = new Tooltip();
    public String tooltipText = "";
    public Integer mode = 0;
    public Integer fixedPos = 3;

    public Tooltip() {
    }

    public void render(DrawContext drawContext, TextRenderer textRenderer, int mouseX, int mouseY) {
        if (this.tooltipText != null && !this.tooltipText.isEmpty()) {
            int windWidth = drawContext.getScaledWindowWidth();
            int winHeight = drawContext.getScaledWindowHeight();
            int textWidth = Math.round(FontManager.fxfontRenderer.getStringWidth(this.tooltipText));
            if (this.mode == 0) {
                this.renderTooltip(drawContext,  tooltipText, mouseX + 1, mouseY - 1);
            } else if (this.mode == 1) {
                switch (fixedPos) {
                    case 0 ->
                        //Top-left
                            this.renderTooltip(drawContext,  tooltipText, 0, 13);
                    case 1 ->
                        //Top-right
                            this.renderTooltip(drawContext,  tooltipText, windWidth - textWidth - 4, 13);
                    case 2 ->
                        //Bottom-left
                            this.renderTooltip(drawContext,  tooltipText, 0, winHeight);
                    case 3 ->
                        //Bottom-right
                            this.renderTooltip(drawContext,  tooltipText, windWidth - textWidth - 4, winHeight);
                    case 4 ->
                        //Center
                            this.renderTooltip(drawContext,  tooltipText, windWidth / 2 - (textWidth - 4) / 2, winHeight);
                }
            } else if (this.mode == 2) {
                drawContext.drawTooltip(textRenderer, Text.of(this.tooltipText), mouseX, mouseY);
            }
        }
        this.tooltipText = "";
    }


    private void renderTooltip(DrawContext drawContext, String text, int x, int y) {
        int textWidth = Math.round(FontManager.fxfontRenderer.getStringWidth(text));
        float textHeight = FontManager.fxfontRenderer.getStringHeight("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        float textY = y - textHeight; // Center the text vertically
        Renderer2D.drawRectangle(drawContext, x, y - 13, (textWidth + 4), 13, 0xAA000000);
        Renderer2D.drawOutlineBox(drawContext, x - 1, y - 13, (textWidth + 5), 14, 1, Color.GRAY.getRGB());
        FontManager.fxfontRenderer.drawString(drawContext.getMatrices(), text, x + 2, textY, ColorManager.INSTANCE.defaultTextColor(),10f);
        //drawContext.drawText(textRenderer, text, x + 2, y - 10, ColorManager.INSTANCE.defaultTextColor(), true);
    }

    public void changeText(String str) {
        this.tooltipText = str;
    }
}