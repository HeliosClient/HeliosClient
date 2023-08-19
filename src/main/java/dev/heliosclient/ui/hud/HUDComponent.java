package dev.heliosclient.ui.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class HUDComponent {
    protected int x;
    protected int y;
    protected boolean active;
    public String name;
    protected int textWidth;
    protected boolean useScaledWidth;
    protected boolean useScaledHeight;

    public HUDComponent(int x, int y, boolean active, String name, boolean useScaledWidth, boolean useScaledHeight) {
        this.x = x;
        this.y = y;
        this.active = active;
        this.name = name;
        this.useScaledWidth = useScaledWidth;
        this.useScaledHeight = useScaledHeight;
    }

    public abstract void render(DrawContext drawContext);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public boolean isMouseOver(int mouseX, int mouseY) {
        int textHeight = 9;
        MinecraftClient mc = MinecraftClient.getInstance();
        int yPosition = useScaledHeight ? mc.getWindow().getScaledHeight() - y : y;
        int xPosition = useScaledWidth ? mc.getWindow().getScaledWidth() - x : x;
        return (mouseX > xPosition && mouseX < xPosition + this.getTextWidth()) && (mouseY > yPosition && mouseY < yPosition + textHeight);
    }


    public void toggle() {
        this.active = !active;
    }


    public int getTextWidth() {
        return textWidth;
    }


}
