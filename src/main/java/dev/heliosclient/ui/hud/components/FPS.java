package dev.heliosclient.ui.hud.components;

import dev.heliosclient.HeliosClient;
import dev.heliosclient.ui.hud.HUDComponent;
import dev.heliosclient.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FPS extends HUDComponent {
    private MinecraftClient mc = MinecraftClient.getInstance();

    public FPS() {
        super(2,12, true, "FPS",false ,false);
    }

    @Override
    public void render(DrawContext drawContext) {
        String text = "FPS: " + ColorUtils.gray + mc.fpsDebugString.split(" ")[0];
        this.textWidth = mc.textRenderer.getWidth(text);
        drawContext.drawTextWithShadow(mc.textRenderer, text, x, y, HeliosClient.uiColorA);
    }
}

