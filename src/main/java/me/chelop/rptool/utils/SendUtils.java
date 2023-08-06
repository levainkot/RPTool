package me.chelop.rptool.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SendUtils {

    private final FileConfiguration config;

    public SendUtils(FileConfiguration config) {
        this.config = config;
    }

    public void send(CommandSender sender, Command command, String[] args, String message, String globalMessage,
                     boolean globalCommand, String originalCommand, String configCommand, int defRange) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) {
            errorUtils.showError(sender, "player-only", "&cОшибка: Можно использовать только игроку.");
            return;
        }
        if (args.length < 1) {
            errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов.");
            return;
        }

        String commandName = command.getName();
        Player player = (Player) sender;
        boolean heard = false;

        for (Player players : player.getWorld().getPlayers()) {
            if (commandName.equalsIgnoreCase(originalCommand)) {
                if (!config.getBoolean("commands." + configCommand + ".enable", true))
                    return;

                if (config.getBoolean("commands." + configCommand + ".range.enable", true)) {
                    int range = config.getInt("commands." + configCommand + ".range.range", defRange);
                    boolean playersInRange = player.getWorld().getPlayers().stream()
                            .anyMatch(p -> p != player && player.getLocation().distanceSquared(p.getLocation()) <= range);

                    if (playersInRange) {
                        players.sendMessage(message);
                        heard = true;
                    }
                } else {
                    players.sendMessage(message);
                    heard = true;
                }
            } else if (commandName.equalsIgnoreCase("g" + originalCommand) && globalCommand) {
                if (config.getBoolean("global-commands." + configCommand + ".enable", true)) {
                    players.sendMessage(globalMessage);
                    heard = true;
                } else {
                    return;
                }
            }
        }

        if (!heard) {
            player.sendMessage(message);
            errorUtils.showError(sender, "not-heard", "&eНикто не услышал тебя.");
        }
    }
}
