package dev.heliosclient.ui.hud.components;

import dev.heliosclient.HeliosClient;
import dev.heliosclient.ui.hud.HUDComponent;
import dev.heliosclient.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Ping extends HUDComponent {
    private MinecraftClient mc = MinecraftClient.getInstance();

    public Ping() {
        super(2, 22,  true, "Ping", false ,false);
    }

    @Override
    public void render(DrawContext drawContext) {
        String text = "Ping: " + ColorUtils.gray + (mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()) == null ? 0 : mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency());
        this.textWidth = mc.textRenderer.getWidth(text);
        drawContext.drawTextWithShadow(mc.textRenderer, text, x, y, HeliosClient.uiColorA);
    }
}
