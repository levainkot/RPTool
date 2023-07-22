package me.chelop.rptool.cmds;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.ErrorUtils;
import me.chelop.rptool.utils.ReplaceUtils;

import java.util.Random;

import static me.chelop.rptool.utils.ErrorUtils.nullError;

public class TryCmd implements CommandExecutor {
    private final FileConfiguration config;

    public TryCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) return errorUtils.showError(sender, "player-only");

        if (!sender.hasPermission("rt.rp")) return errorUtils.showError(sender, "no-permissions");

        if (args.length >= 1) {

            String action = String.join(" ", args);
            Random random = new Random();
            String radius = config.getString("range.enabled"); // Get the value from the config

            boolean result = radius != null && radius.equalsIgnoreCase("true"); // Is the value from the "radius" configuration true?
            boolean foundTarget = false; // Variable, to check the radius

            Player player = (Player) sender;
            World world = player.getWorld(); // Get the world in which the sender is located

            String string = config.getString("try");

            if (string == null) return nullError(sender, "try");
            string = ReplaceUtils.replace(string, "%player%", player.getName(), "%action%", action);

            if (result) { // If true, then a block of code is executed with sending a message to a certain radius

                for (Player players : world.getPlayers()) {
                    String value = config.getString("range.range");

                    if (value == null) return nullError(sender, "range.range");
                    int randomValue = random.nextInt(11);

                    if (randomValue < 5) {

                        if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < Integer.parseInt(value)) {
                            string = string.replaceAll("%result%", ChatColor.GREEN + "Удачно");
                            string = ChatColor.translateAlternateColorCodes('&', string);

                            foundTarget = true;
                            players.sendMessage(string);
                        }
                    } else {

                        if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < Integer.parseInt(value)) {
                            string = string.replaceAll("%result%", ChatColor.RED + "Неудачно");
                            string = ChatColor.translateAlternateColorCodes('&', string);

                            foundTarget = true;
                            players.sendMessage(string);
                        }
                    }
                }
            } else { // If false, then a block of code is executed with sending a message without a radius

                for (Player players : world.getPlayers()) {
                    int randomValue = random.nextInt(11);

                    if (randomValue < 5) {
                        string = string.replaceAll("%result%", ChatColor.GREEN + "Удачно");
                        string = ChatColor.translateAlternateColorCodes('&', string);

                        players.sendMessage(string);
                    }
                    else {
                        string = string.replaceAll("%result%", ChatColor.RED + "Неудачно");
                        string = ChatColor.translateAlternateColorCodes('&', string);

                        players.sendMessage(string);
                    }
                    return true;
                }
            }

            if (!foundTarget) return errorUtils.showError(sender, "no-heard");
        } else {
            return errorUtils.showError(sender, "not-enough-arguments");
        }

        return true;
    }
}
