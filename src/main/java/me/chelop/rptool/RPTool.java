package me.chelop.rptool;

import me.chelop.rptool.cmds.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public final class RPTool extends JavaPlugin {
    @Override
    public void onEnable() {
        //    Loading & saving the config
        File configFile = new File(getDataFolder(), "config.yml");
        ConfigManager configManager = new ConfigManager(configFile, getConfig(), this);
        configManager.saveDefaultConfig();
        FileConfiguration config = configManager.getConfig();

        //    Reloading the config
        getCommand("rt").setExecutor(new ReloadCmd(config, configFile));

        //    Commands
        getCommand("do").setExecutor(new DoCmd(config));
        getCommand("me").setExecutor(new MeCmd(config));
        getCommand("todo").setExecutor(new TodoCmd(config));
        getCommand("try").setExecutor(new TryCmd(config));
        getCommand("n").setExecutor(new NonRPCmd(config));
        getCommand("w").setExecutor(new WhisperCmd(config));
    }
}
