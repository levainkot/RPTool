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

import static me.chelop.rptool.utils.ErrorUtils.nullError;

public class TodoCmd implements CommandExecutor {
    private final FileConfiguration config;

    public TodoCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) {
            return errorUtils.showError(sender, "player-only");
        }

        if (!sender.hasPermission("rt.rp")) {
            return errorUtils.showError(sender, "no-permissions");
        }

        if (args.length >= 1) {
            String[] splitArgs = args[0].split("\\*");

            if (splitArgs.length < 2) {
                return errorUtils.showError(sender, "not-enough-arguments");
            }

            String message = splitArgs[0];
            String action = splitArgs[1];
            String string = config.getString("todo");

            if (string == null) {
                return nullError(sender, "todo");
            }

            Player player = (Player) sender;
            World world = player.getWorld();

            string = ReplaceUtils.replace(string, "%player%", player.getName(), "%action%", action, "%message%", message);
            string = ChatColor.translateAlternateColorCodes('&', string);

            String radius = config.getString("range.enabled");
            boolean result = radius != null && radius.equalsIgnoreCase("true");
            boolean foundTarget = false;

            if (result) {
                String value = config.getString("range.range");

                if (value == null) {
                    return nullError(sender, "range.range");
                }

                int range = Integer.parseInt(value);

                for (Player players : world.getPlayers()) {
                    if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                        foundTarget = true;
                        player.sendMessage(string);
                    }
                }
            } else {
                for (Player players : world.getPlayers()) {
                    players.sendMessage(string);
                }
                return true;
            }

            if (!foundTarget) {
                return errorUtils.showError(sender, "no-heard");
            }
        } else {
            return errorUtils.showError(sender, "not-enough-arguments");
        }

        return true;
    }
}