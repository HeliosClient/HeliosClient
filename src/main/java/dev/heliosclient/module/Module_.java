package dev.heliosclient.module;

import dev.heliosclient.module.settings.BooleanSetting;
import dev.heliosclient.module.settings.KeyBind;
import dev.heliosclient.module.settings.Setting;
import dev.heliosclient.ui.ModulesListOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.MovementType;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;


public abstract class Module_ {
    protected static MinecraftClient mc = MinecraftClient.getInstance();
    public String name;
    public String description;
    public Category category;
    public ArrayList<Setting> settings;

    public BooleanSetting chatFeedback = new BooleanSetting("Enable chat feedback", false);
    public BooleanSetting showInModulesList = new BooleanSetting("Show in Modules List", true);
    public KeyBind keyBind = new KeyBind("Keybind", 0);
    public BooleanSetting active = new BooleanSetting("Active", false);

    public Module_(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        settings = new ArrayList<>();
    }

    public void onEnable() {
        ModulesListOverlay.INSTANCE.update();
        if (chatFeedback.value) {
            assert mc.player != null;
            mc.player.sendMessage(Text.literal("[§4Helios] " + this.name + " was enabled."));
        }
    }
    public boolean isActive(){
        return active.value;
    }

    public void onDisable() {
        ModulesListOverlay.INSTANCE.update();
        if (chatFeedback.value) {
            assert mc.player != null;
            mc.player.sendMessage(Text.literal("[§4Helios] " + this.name + " was disabled."));
        }
    }

    public void onMotion(MovementType type, Vec3d movement) {
    }

    public void onTick() {
    }

    public void toggle() {
        active.value = !active.value;
        if (active.value) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public Integer getKeybind() {
        return keyBind.value;
    }

    public void setKeybind(Integer keycode) {
        keyBind.value = keycode;
    }

    public void onLoad() {
        settings.add(showInModulesList);
        settings.add(chatFeedback);
        settings.add(keyBind);
        settings.add(active);
    }
}
