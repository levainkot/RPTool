package me.chelop.rptool.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

import static me.chelop.rptool.utils.ErrorUtils.nullError;

public class ReloadCmd implements CommandExecutor {

    private final FileConfiguration config;
    private final File configFile;

    public ReloadCmd(FileConfiguration config, File configFile) {
        this.config = config;
        this.configFile = configFile;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || !args[0].equals("reload")) {
            sender.sendMessage(ChatColor.RED + "Ошибка: Некорректный аргумент. Используй /rt reload для перезагрузки конфигурации.");
            return false;
        }

        try {
            config.load(configFile); // Reloading the configuration
            String msg = config.getString("config-reload");
            if (msg == null) {
                return nullError(sender, "config-reload");
            }

            msg = ChatColor.translateAlternateColorCodes('&', msg);
            sender.sendMessage(msg);

        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + "Ошибка при перезагрузке конфигурации: " + e.getMessage());
        } catch (InvalidConfigurationException e) {
            sender.sendMessage(ChatColor.RED + "Ошибка при загрузке конфигурации: " + e.getMessage());
        }

        return true;
    }
}
