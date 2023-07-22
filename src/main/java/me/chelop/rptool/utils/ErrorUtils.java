package me.chelop.rptool.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ErrorUtils {
    private final FileConfiguration config;

    public ErrorUtils(FileConfiguration config) {
        this.config = config;
    }

    public static boolean nullError(CommandSender sender, String errorKey) {
        sender.sendMessage(ChatColor.RED + "Ошибка: Значение '" + errorKey + "' не найдено в конфигурации.");
        return true;
    }
    public boolean showError(CommandSender sender, String errorKey) {
        String error = this.config.getString(errorKey);

        if (error == null) {
            return nullError(sender, errorKey);
        }
        error = ChatColor.translateAlternateColorCodes('&', error);
        sender.sendMessage(error);

        return true;
    }

}