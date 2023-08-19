package dev.heliosclient.ui.hud;

import dev.heliosclient.system.ColorManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class HUDButton {
    public int hoverAnimationTimer;
    public HUDComponent component;
    public int x, y, width, height = 0;
    private int alpha = 255;
    private int hovertimer = 0;
    private float fade = 0.0f;
    private boolean fading = false;

    public HUDButton(HUDComponent component) {
        this.component = component;
        this.width = 96;
        this.height = 14;
    }

    public void setFading(boolean fading) {
        this.fading = fading;
    }

    public boolean hasFaded() {
        return fading;
    }

    public float getFade() {
        return fade;
    }

    public void setFade(float fade) {
        this.fade = fade;
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, int x, int y, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;

        fade += 0.1f;
        if (fade >= 1.0f) {
            fade = 1.0f;
        }


        if (hovered(mouseX, mouseY)) {
            hovertimer++;
            hoverAnimationTimer = Math.min(hoverAnimationTimer + 1, 20);
        } else {
            hovertimer = 0;
            hoverAnimationTimer = Math.max(hoverAnimationTimer - 1, 0);
        }

        int fillColor = (int) (34 + 0.85 * hoverAnimationTimer);
        alpha = (int) (fade * 255);


        drawContext.fill(x, y, x + width, y + height, new Color(fillColor, fillColor, fillColor, alpha).getRGB());
        drawContext.drawText(textRenderer, component.name, x + 3, y + 3, component.active ? ColorManager.INSTANCE.clickGuiSecondary() : ColorManager.INSTANCE.defaultTextColor() , false);

    }

    public boolean hovered(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button, boolean collapsed) {
        if (!collapsed) {
            if (hovered(mouseX, mouseY)) {
                if (button == 0) {
                  component.toggle();
                }
            }
        }
        return false;
    }
}

