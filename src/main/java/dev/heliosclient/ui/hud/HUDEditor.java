package dev.heliosclient.ui.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class HUDEditor extends Screen {
    private HUDComponent selectedComponent = null;
    private int offsetX = 0;
    private int offsetY = 0;
    ArrayList<HUDCategoryPane> HUDcategoryPanes;

    public HUDEditor(Text title) {
        super(Text.literal("HUD Editor"));
        HUDcategoryPanes = new ArrayList<>();
        HUDcategoryPanes.add(new HUDCategoryPane(100, 100, false));
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);

        for (HUDComponent component : HUDOverlay.INSTANCE.getComponents()) {
            if(component.active) {
               component.render(drawContext);
            }
        }

        for (HUDCategoryPane category : HUDcategoryPanes)
        {
            category.render(drawContext, mouseX, mouseY, delta, textRenderer);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (HUDComponent component : HUDOverlay.INSTANCE.getComponents()) {
            if (component.isMouseOver((int) mouseX, (int) mouseY)) {
                selectedComponent = component;
                offsetX = (int) mouseX - component.getX();
                offsetY = (int) mouseY - component.getY();
                break;
            }
        }

        for (HUDCategoryPane category : HUDcategoryPanes) {
            category.mouseClicked((int) mouseX, (int) mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        selectedComponent = null;
        for (HUDCategoryPane category : HUDcategoryPanes) {
            category.mouseReleased((int) mouseX, (int) mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (selectedComponent != null) {
            if (selectedComponent.useScaledWidth) {
                deltaX = -deltaX;
            }

            if (selectedComponent.useScaledHeight) {
                deltaY = -deltaY;
            }

            selectedComponent.setX(selectedComponent.getX() + (int) deltaX);
            selectedComponent.setY(selectedComponent.getY() + (int) deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }




}

