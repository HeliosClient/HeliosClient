package dev.heliosclient.system;

import com.google.gson.Gson;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import dev.heliosclient.HeliosClient;
import dev.heliosclient.hud.HudElement;
import dev.heliosclient.hud.HudElementData;
import dev.heliosclient.hud.HudElementList;
import dev.heliosclient.managers.CategoryManager;
import dev.heliosclient.managers.HudManager;
import dev.heliosclient.managers.ModuleManager;
import dev.heliosclient.module.Module_;
import dev.heliosclient.module.settings.Setting;
import dev.heliosclient.module.settings.SettingGroup;
import dev.heliosclient.ui.clickgui.CategoryPane;
import dev.heliosclient.ui.clickgui.ClickGUIScreen;
import dev.heliosclient.util.FileUtils;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mojang.text2speech.Narrator.LOGGER;

public class Config {
    private static final Gson gson = new Gson();
    public Map<String, Object> moduleConfigMap = new HashMap<>();
    public Map<String, Object> hudConfigMap = new HashMap<>();

    public Map<String, Object> clientConfigMap = new HashMap<>();
    public TomlWriter tomlWriter = new TomlWriter.Builder()
            .indentTablesBy(2)
            .build();
    public Toml moduleToml;
    public Toml clientToml;
    public Toml hudToml;

    MinecraftClient mc = MinecraftClient.getInstance();
    public File configDir = new File(mc.runDirectory.getPath() + "/heliosclient");
    public File configFile = new File(configDir, "config.toml");
    public File moduleConfigFile = new File(configDir, "modules.toml");
    public File hudConfigFile = new File(configDir, "hud.toml");


    public Config() {
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }


    public void getDefaultModuleConfig() {
        final AtomicInteger[] xOffset = {new AtomicInteger(4)};
        final int[] yOffset = {4};
        Map<String, Object> categoryPaneMap = new HashMap<>();
        CategoryManager.getCategories().forEach((s, category) -> {
            Map<String, Object> paneConfigMap = new HashMap<>();
            if (xOffset[0].get() > 400) {
                xOffset[0].set(4);
                yOffset[0] = 128;
            }
            paneConfigMap.put("x", xOffset[0].get());
            paneConfigMap.put("y", yOffset[0]);
            paneConfigMap.put("collapsed", false);
            for (Module_ module : ModuleManager.INSTANCE.getModulesByCategory(category)) {
                Map<String, Object> ModuleConfig = new HashMap<>();
                for (SettingGroup settingGroup : module.settingGroups) {
                    for (Setting setting : settingGroup.getSettings()) {
                        if (setting.name != null) {
                            ModuleConfig.put(setting.name.replace(" ", ""), setting.saveToToml(new ArrayList<>()));
                        }
                    }
                }
                paneConfigMap.put(module.name.replace(" ", ""), ModuleConfig);
            }
            categoryPaneMap.put(category.name, paneConfigMap);
            moduleConfigMap.put("panes", categoryPaneMap);
            xOffset[0].addAndGet(100);
        });
    }

    public void getModuleConfig() {
        try {
            Map<String, Object> categoryPaneMap = new HashMap<>();
            CategoryManager.getCategories().forEach((s, category) -> {
                Map<String, Object> paneConfigMap = new HashMap<>();
                CategoryPane categoryPane = CategoryManager.findCategoryPane(category);
                if (categoryPane != null) {
                    paneConfigMap.put("x", categoryPane.x);
                    paneConfigMap.put("y", categoryPane.y);
                    paneConfigMap.put("collapsed", categoryPane.collapsed);
                    for (Module_ module : ModuleManager.INSTANCE.getModulesByCategory(category)) {
                        Map<String, Object> ModuleConfig = new HashMap<>();
                        for (SettingGroup settingGroup : module.settingGroups) {
                            for (Setting setting : settingGroup.getSettings()) {
                                if (setting.name != null) {
                                    ModuleConfig.put(setting.name.replace(" ", ""), setting.saveToToml(new ArrayList<>()));
                                }
                            }
                        }
                        paneConfigMap.put(module.name.replace(" ", ""), ModuleConfig);
                    }
                    categoryPaneMap.put(category.name, paneConfigMap);
                    moduleConfigMap.put("panes", categoryPaneMap);
                } else {
                    this.getDefaultModuleConfig();
                }
            });
        } catch (Exception e) {
            LOGGER.error("Error occurred while getting module config. Loading default config.", e);
            this.getDefaultModuleConfig();
        }
    }

    public void getHudConfig() {
        try {
            Map<String, Object> hudElements = new HashMap<>();

            int a = 0;
            for (HudElement hudElement : HudManager.INSTANCE.hudElements) {
                hudElements.put("element_" + a, hudElement.saveToToml(new ArrayList<>()));
                a++;
            }

            hudConfigMap.put("hudElements", hudElements);
        } catch (Exception e) {
            LOGGER.error("Error occurred while getting Hud config.", e);
        }
    }
    public void loadHudElements() {
        HudManager.INSTANCE.hudElements.clear();
        Toml toml = hudToml.getTable("hudElements");
        if (toml != null) {
            toml.toMap().forEach((string, object) -> {
                Toml hudElementTable =  toml.getTable(string);
                if(hudElementTable.contains("name")) {
                    HudElementData hudElementData = HudElementList.INSTANCE.elementDataMap.get(hudElementTable.getString("name"));
                   HudElement hudElement = hudElementData.create();
                   if(hudElement != null){
                       hudElement.loadFromToml(hudElementTable.toMap(),hudElementTable);
                       HudManager.INSTANCE.addHudElement(hudElement);
                   }
                }
            });
        }
    }

    public void load() {
        try {
            moduleToml = new Toml().read(moduleConfigFile);
            clientToml = new Toml().read(configFile);
            hudToml = new Toml().read(hudConfigFile);
            if (moduleToml != null && clientToml != null && hudToml != null) {
                moduleConfigMap = moduleToml.toMap();
                clientConfigMap = clientToml.toMap();
                hudConfigMap = hudToml.toMap();
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while loading config. Loading default config.", e);
            this.getDefaultModuleConfig();
        }
    }


    public boolean shouldLoadDefaultConfig() {
        return !FileUtils.doesFileInPathExist(this.configFile.getPath()) || !FileUtils.doesFileInPathExist(this.moduleConfigFile.getPath()) || !FileUtils.doesFileInPathExist(this.hudConfigFile.getPath());
    }

    public void loadConfig() {
        ModuleManager.INSTANCE = new ModuleManager();
        if (shouldLoadDefaultConfig()) {
            LOGGER.info("Loading default config...");
            this.getDefaultModuleConfig();
            this.save();
        } else {
            this.getModuleConfig();
        }
        this.getClientConfig();
        this.getHudConfig();
        this.load();
        ClickGUIScreen.INSTANCE = new ClickGUIScreen();
    }

    public void loadModules() {
        CategoryManager.getCategories().forEach((s, category) -> {
            for (Module_ m : ModuleManager.INSTANCE.getModulesByCategory(category)) {
                for (SettingGroup settingGroup : m.settingGroups) {
                    for (Setting setting : settingGroup.getSettings()) {
                        Toml newToml = moduleToml.getTable("panes").getTable(category.name).getTable(m.name.replace(" ", ""));
                        if (newToml != null) {
                            setting.loadFromToml(newToml.toMap(), newToml);
                        }
                    }
                }
            }
        });
    }

    public void loadClientConfigModules() {
        for (SettingGroup settingGroup : HeliosClient.CLICKGUI.settingGroups) {
            for (Setting setting : settingGroup.getSettings()) {
                if (setting.name != null) {
                    Toml settingsToml = clientToml.getTable("settings");
                    if (settingsToml != null)
                        setting.loadFromToml(settingsToml.toMap(), settingsToml);
                }
            }
        }
    }

    public void getClientConfig() {
        clientConfigMap.put("prefix", ".");
        Map<String, Object> ModuleConfig = new HashMap<>();
        for (SettingGroup settingGroup : HeliosClient.CLICKGUI.settingGroups) {
            for (Setting setting : settingGroup.getSettings()) {
                if (setting.name != null) {
                    ModuleConfig.put(setting.name.replace(" ", ""), setting.saveToToml(new ArrayList<>()));
                }
            }
        }
        clientConfigMap.put("settings", ModuleConfig);
    }

    public void save() {
        try {
            if (!FileUtils.doesFileInPathExist(this.configFile.getPath())) configFile.createNewFile();
            if (!FileUtils.doesFileInPathExist(this.moduleConfigFile.getPath())) moduleConfigFile.createNewFile();
            if (!FileUtils.doesFileInPathExist(this.hudConfigFile.getPath())) hudConfigFile.createNewFile();

            getHudConfig();
            tomlWriter.write(clientConfigMap, configFile);
            tomlWriter.write(moduleConfigMap, moduleConfigFile);
            tomlWriter.write(hudConfigMap, hudConfigFile);
        } catch (Exception feelings) {
            feelings.printStackTrace();
        }
    }
}
