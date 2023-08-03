package me.chelop.rptool;

import me.chelop.rptool.cmds.*;
import me.chelop.rptool.listeners.SmileListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public final class RPTool extends JavaPlugin {
    @Override
    public void onEnable() {
        // Loading & saving the configuration
        File configFile = new File(getDataFolder(), "config.yml");
        ConfigManager configManager = new ConfigManager(configFile, getConfig(), this);
        configManager.saveDefaultConfig();
        FileConfiguration config = configManager.getConfig();

        // Reloading the configuration
        registerCommand("rt", new ReloadCmd(config, configFile));

        // Commands
        registerCommand("do", new DoCmd(config));
        registerCommand("me", new MeCmd(config));
        registerCommand("todo", new TodoCmd(config));
        registerCommand("try", new TryCmd(config));
        registerCommand("s", new ScreamCmd(config));
        registerCommand("n", new NonRPCmd(config));
        registerCommand("w", new WhisperCmd(config));

        // Global Commands
        registerCommand("gdo", new DoCmd(config));
        registerCommand("gme", new MeCmd(config));
        registerCommand("gtodo", new TodoCmd(config));
        registerCommand("gtry", new TryCmd(config));
        registerCommand("gn", new NonRPCmd(config));

        // TabCompleter
        registerTabCompleter("rt");

        registerTabCompleter("do");
        registerTabCompleter("me");
        registerTabCompleter("todo");
        registerTabCompleter("try");
        registerTabCompleter("s");
        registerTabCompleter("n");
        registerTabCompleter("w");

        registerTabCompleter("gdo");
        registerTabCompleter("gme");
        registerTabCompleter("gtodo");
        registerTabCompleter("gtry");
        registerTabCompleter("gn");

        // Listeners
        SmileListener smileListener = new SmileListener(config);
        getServer().getPluginManager().registerEvents(smileListener, this);
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        getCommand(commandName).setExecutor(executor);
    }

    private void registerTabCompleter(String commandName) {
        getCommand(commandName).setTabCompleter(new Completer());
    }
}
