package dev.heliosclient.module.modules;

import dev.heliosclient.module.Category;
import dev.heliosclient.module.Module_;
import dev.heliosclient.HeliosClient;
import dev.heliosclient.module.settings.BooleanSetting;
import dev.heliosclient.module.settings.ColorSetting;
import dev.heliosclient.module.settings.ScreenButtonSetting;
import dev.heliosclient.ui.hud.HUDEditor;
import dev.heliosclient.util.ColorUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.awt.*;

public class HUDModule extends Module_
{
    public ColorSetting colorSetting = new ColorSetting("Color", "Color of HUD.", this, new Color(241, 83, 92, 255).getRGB());
    BooleanSetting Rainbow = new BooleanSetting("Rainbow", "Toggles rainbow effect for HUD.", this,false);

    ScreenButtonSetting HudBtn = new ScreenButtonSetting("HUD Editor", new HUDEditor(Text.literal("Hud Editor")));
    public int hudColor = colorSetting.value;

    public HUDModule()
    {
        super("HUD", "The HeliosClient HUD. Toggle to update.", Category.RENDER);
        this.active.value = true;
        this.showInModulesList.value = false;
        settings.add(HudBtn);
        settings.add(colorSetting);
        settings.add(Rainbow);
    }    

    @Override
    public void onEnable()
    {
        super.onEnable();
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (Rainbow.value)
            {
                HeliosClient.uiColorA = ColorUtils.getRainbowColor().getRGB();
                HeliosClient.uiColor = ColorUtils.getRainbowColor().getRGB();
            }
           else {
                    HeliosClient.uiColorA = colorSetting.value;
                    HeliosClient.uiColor = colorSetting.value;
            }
        });
    }

}
