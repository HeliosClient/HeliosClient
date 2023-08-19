package dev.heliosclient.ui.hud;

import dev.heliosclient.HeliosClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class HUDCategoryPane {
    public int x, y, height, width = 96;
    public boolean collapsed = false;
    int startX, startY;
    boolean dragging = false;
    ArrayList<HUDButton> hudButtons;

    public HUDCategoryPane(int initialX, int initialY, boolean collapsed) {
        this.x = initialX;
        this.y = initialY;
        this.collapsed = collapsed;
        hudButtons = new ArrayList<HUDButton>();
        for (HUDComponent h : HUDOverlay.INSTANCE.getComponents()) {
            hudButtons.add(new HUDButton(h));
        }
        if (hudButtons.size() == 0) collapsed = true;
        height = (hudButtons.size() * 12) + 18;
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta, TextRenderer textRenderer) {
        if (dragging) {
            x = mouseX - startX;
            y = mouseY - startY;
        }

        if (collapsed)
        {
            for (HUDButton h : hudButtons) {
                h.setFading(false);
            }
        }

        drawContext.fill(x, y + 16, x + width, y + 18, HeliosClient.categoryColor);
        drawContext.fill(x, y, x + width, y + 16, 0xFF1B1B1B);
        drawContext.drawText(textRenderer, "HUD", x + 4, y + 4, 0xFFFFFFFF, false);
        drawContext.drawText(textRenderer, collapsed ? "+" : "-", x + width - 11, y + 4, 0xFFFFFFFF, false);
        if (!collapsed) {
            int buttonYOffset = y + 18;
            for (HUDButton h : hudButtons) {
                if (h.getFade() >= 1.0f && !h.hasFaded()) {
                    h.setFade(0.0f);
                    h.setFading(true);
                }
                h.render(drawContext, mouseX, mouseY, x, buttonYOffset, textRenderer);
                buttonYOffset += 14;
            }
        }
    }

    public boolean hovered(double mouseX, double mouseY) {
        return mouseX > x + 2 && mouseX < x + (width - 2) && mouseY > y + 2 && mouseY < y + 14;
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        for (HUDButton hudButton : hudButtons) {
            if (hudButton.mouseClicked(mouseX, mouseY, button, collapsed)) return;
        }
        if (hovered(mouseX, mouseY) && button == 1) collapsed = !collapsed;
        else if (hovered(mouseX, mouseY) && button == 0) {
            startX = mouseX - x;
            startY = mouseY - y;
            dragging = true;
        }
        if (button == 2) {
            startX = mouseX - x;
            startY = mouseY - y;
            dragging = true;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        dragging = false;
    }
}
