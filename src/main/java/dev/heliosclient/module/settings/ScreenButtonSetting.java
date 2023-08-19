package dev.heliosclient.module.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class ScreenButtonSetting extends Setting
{
    public final Screen screen;

    public ScreenButtonSetting(String name, Screen screen)
    {
        this.name = name;
        this.screen = screen;
    }
    @Override
    public void render(DrawContext drawContext, int x, int y, int mouseX, int mouseY, TextRenderer textRenderer)
    {
        super.render(drawContext, x, y, mouseX, mouseY, textRenderer);

        drawContext.drawTextWithShadow(textRenderer, name, x+2, y+8, 0xFFFFFF);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button)
    {
        if (hovered((int)mouseX, (int)mouseY) && button == 0)
        {
            MinecraftClient.getInstance().setScreen(screen);
        }
    }
}

