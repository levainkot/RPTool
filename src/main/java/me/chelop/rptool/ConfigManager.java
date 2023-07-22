package me.chelop.rptool;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigManager {
    private final File configFile;
    private final FileConfiguration config;
    private final Plugin plugin;

    public ConfigManager(File configFile, FileConfiguration config, Plugin plugin) {
        this.configFile = configFile;
        this.config = config;
        this.plugin = plugin;
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource(configFile.getName(), false);
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
