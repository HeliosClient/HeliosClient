package dev.heliosclient.ui.hud.components;

import dev.heliosclient.HeliosClient;
import dev.heliosclient.ui.hud.HUDComponent;
import dev.heliosclient.util.ColorUtils;
import dev.heliosclient.util.MathUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

public class Speed extends HUDComponent {

    MinecraftClient mc = MinecraftClient.getInstance();

    public Speed() {

        super("Speed",2, 20,true,  false, true);
    }

    @Override
    public void render(DrawContext drawContext) {

        int scaledHeight = mc.getWindow().getScaledHeight();
        String text = "Meters/s: " + ColorUtils.gray + MathUtils.round(moveSpeed(), 2);
        this.textWidth = mc.textRenderer.getWidth(text);
        drawContext.drawTextWithShadow(mc.textRenderer, text, x, scaledHeight - y, HeliosClient.uiColorA);
    }

    private double moveSpeed() {
        Vec3d move = new Vec3d(mc.player.getX() - mc.player.prevX, 0, mc.player.getZ() - mc.player.prevZ).multiply(20);
        return Math.abs(MathUtils.length2D(move));
    }
}
