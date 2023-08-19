package dev.heliosclient.ui.hud.components;

import dev.heliosclient.HeliosClient;
import dev.heliosclient.ui.hud.HUDComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ClientTag extends HUDComponent {
    private MinecraftClient mc = MinecraftClient.getInstance();

    public ClientTag() {
        super("Client Tag",2, 2, true, false ,false);
    }

    @Override
    public void render(DrawContext drawContext) {
        String text = HeliosClient.clientTag + " " + HeliosClient.versionTag;
        this.textWidth = mc.textRenderer.getWidth(text);
        drawContext.drawTextWithShadow(mc.textRenderer, text, x, y, 16777215);
    }
}
