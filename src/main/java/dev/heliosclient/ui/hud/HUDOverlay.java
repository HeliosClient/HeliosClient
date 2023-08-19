package dev.heliosclient.ui.hud;

import dev.heliosclient.ui.hud.components.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class HUDOverlay {
    public static HUDOverlay INSTANCE = new HUDOverlay();
    private MinecraftClient mc = MinecraftClient.getInstance();
    private List<HUDComponent> components = new ArrayList<>();

    public HUDOverlay() {
        addComponent(new FPS());
        addComponent(new Ping());
        addComponent(new Speed());
        addComponent(new Coordinates());
        addComponent(new ClientTag());
    }

    public void addComponent(HUDComponent component) {
        components.add(component);
    }

    public List<HUDComponent> getComponents() {
        return components;
    }

    public void render(DrawContext drawContext) {
        if (mc.options.debugEnabled) return;
        for (HUDComponent component : components) {
            if(component.active) {
                component.render(drawContext);
            }
        }
    }
}

