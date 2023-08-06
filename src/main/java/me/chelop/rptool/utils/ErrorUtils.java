package me.chelop.rptool.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ErrorUtils {
    private final FileConfiguration config;

    public ErrorUtils(FileConfiguration config) {
        this.config = config;
    }

    public boolean showError(CommandSender sender, String errorKey, String def) {
        String error = config.getString(errorKey, def);

        error = ChatColor.translateAlternateColorCodes('&', error);
        sender.sendMessage(error);

        return true;
    }
}