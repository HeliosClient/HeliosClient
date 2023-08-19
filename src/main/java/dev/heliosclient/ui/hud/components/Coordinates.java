package dev.heliosclient.ui.hud.components;

import dev.heliosclient.HeliosClient;
import dev.heliosclient.ui.hud.HUDComponent;
import dev.heliosclient.util.ColorUtils;
import dev.heliosclient.util.MathUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Coordinates extends HUDComponent {
    private MinecraftClient mc = MinecraftClient.getInstance();

    public Coordinates() {
        super(2,10, true, "Coordinates", false ,true);
    }


    @Override
    public void render(DrawContext drawContext) {
        int scaledHeight = mc.getWindow().getScaledHeight();
        String text = "X: " + ColorUtils.gray + MathUtils.round(mc.player.getX(), 1) + ColorUtils.reset +
                " Y: " + ColorUtils.gray + MathUtils.round(mc.player.getY(), 1) + ColorUtils.reset +
                " Z: " + ColorUtils.gray + MathUtils.round(mc.player.getZ(), 1);
        this.textWidth = mc.textRenderer.getWidth(text);
        drawContext.drawTextWithShadow(mc.textRenderer,text, x, scaledHeight - y, HeliosClient.uiColorA);
    }
}
