package dev.heliosclient.module.settings;

import dev.heliosclient.system.ColorManager;
import dev.heliosclient.ui.clickgui.ListSettingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;

public class ListSetting extends Setting {
    public ArrayList < String > value;
    public ArrayList < String > options;
    Screen parentScreen;

    public ListSetting(String name, String description, Screen parentScreen, ArrayList < String > options, ArrayList < String > value) {
        this.name = name;
        this.description = description;
        this.options = options;
        this.parentScreen = parentScreen;
        this.value = value;
    }

    @Override
    public void render(DrawContext drawContext, int x, int y, int mouseX, int mouseY, TextRenderer textRenderer) {
        super.render(drawContext, x, y, mouseX, mouseY, textRenderer);

        drawContext.drawText(textRenderer, name, x + 2, y + 8, ColorManager.INSTANCE.defaultTextColor(), false);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered((int) mouseX, (int) mouseY) && button == 0) {
            MinecraftClient.getInstance().setScreen(new ListSettingScreen(this, parentScreen));
        }
    }

    public boolean isOptionEnabled(String option) {
        return value.contains(option);
    }

}