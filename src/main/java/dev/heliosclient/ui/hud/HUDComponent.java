package dev.heliosclient.ui.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class HUDComponent {
    public String name;
    protected int x;
    protected int y;
    protected boolean active;
    protected boolean useScaledWidth;
    protected boolean useScaledHeight;
    protected int textWidth;

    public HUDComponent( String name, int x, int y, boolean active, boolean useScaledWidth, boolean useScaledHeight) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.active = active;
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

    public void toggle() {
        this.active = !active;
    }


    public int getTextWidth() {
        return textWidth;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        int textHeight = 9;
        MinecraftClient mc = MinecraftClient.getInstance();
        int yPosition = useScaledHeight ? mc.getWindow().getScaledHeight() - y : y;
        int xPosition = useScaledWidth ? mc.getWindow().getScaledWidth() - x : x;
        return (mouseX > xPosition && mouseX < xPosition + this.getTextWidth()) && (mouseY > yPosition && mouseY < yPosition + textHeight);
    }
}
