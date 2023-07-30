package me.chelop.rptool.cmds;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

import static me.chelop.rptool.utils.ErrorUtils.nullError;

public class TodoCmd implements CommandExecutor {
    private final FileConfiguration config;

    public TodoCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        EnableUtils enable = new EnableUtils(config);
        boolean result = enable.check("commands.todo.enable");

        if (!result) {
            return true;
        }

        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) {
            return errorUtils.showError(sender, "player-only");
        }

        if (args.length >= 1) {
            String[] splitArgs = args[0].split("\\*");

            if (splitArgs.length < 2) {
                return errorUtils.showError(sender, "not-enough-arguments");
            }

            String message = splitArgs[0];
            String action = splitArgs[1];
            String messageTodo = config.getString("commands.todo.message");

            if (messageTodo == null) {
                return nullError(sender, "commands.todo.message");
            }

            Player player = (Player) sender;
            World world = player.getWorld();

            messageTodo = ReplaceUtils.replace(messageTodo, "%player%", player.getName(), "%action%", action, "%message%", message);
            messageTodo = ChatColor.translateAlternateColorCodes('&', messageTodo);

            boolean resultRange = enable.check("commands.todo.range.enable");
            boolean foundTarget = false;

            String commandName = command.getName();

            for (Player players : world.getPlayers()) {
                if (commandName.equalsIgnoreCase("todo")) {
                    if (resultRange) {
                        String value = config.getString("commands.todo.range.range");

                        if (value == null) {
                            return nullError(sender, "commands.todo.range.range");
                        }

                        int range = Integer.parseInt(value);

                        if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                            foundTarget = true;
                            players.sendMessage(messageTodo);
                            player.sendMessage(messageTodo);
                        }
                    } else {
                        players.sendMessage(messageTodo);
                        foundTarget = true;
                    }
                } else if (commandName.equalsIgnoreCase("gtodo")) {
                    boolean globalEnabled = enable.check("global-commands.todo.enable");

                    if (globalEnabled) {
                        String globalCmd = config.getString("global-commands.todo.message");
                        globalCmd = ReplaceUtils.replace(globalCmd, "%player%", player.getName(), "%action%", action, "%message%", message);
                        globalCmd = ChatColor.translateAlternateColorCodes('&', globalCmd);

                        players.sendMessage(globalCmd);
                        foundTarget = true;
                    }
                    else {
                        return true;
                    }
                }
            }

            if (!foundTarget) {
                return errorUtils.showError(sender, "not-heard");
            }
        } else {
            return errorUtils.showError(sender, "not-enough-arguments");
        }
        return true;
    }
}