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

public class MeCmd implements CommandExecutor {

    private final FileConfiguration config;

    public MeCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        EnableUtils enable = new EnableUtils(config);
        boolean result = enable.check("commands.me.enable");

        if (!result) {
            return true;
        }

        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) {
            return errorUtils.showError(sender, "player-only");
        }

        if (args.length >= 1) {
            String action = String.join(" ", args);
            boolean resultRange = enable.check("commands.me.enable");

            boolean foundTarget = false;

            Player player = (Player) sender;
            World world = player.getWorld();

            String messageMe = config.getString("commands.me.message");

            if (messageMe == null) {
                return nullError(sender, "commands.me.message");
            }

            messageMe = ReplaceUtils.replace(messageMe, "%player%", player.getName(), "%action%", action);
            messageMe = ChatColor.translateAlternateColorCodes('&', messageMe);

            String commandName = command.getName();

            for (Player players : world.getPlayers()) {
                if (commandName.equalsIgnoreCase("me")) {
                    if (resultRange) {
                        String value = config.getString("commands.me.range.range");

                        if (value == null) {
                            return nullError(sender, "commands.me.range.range");
                        }

                        int range = Integer.parseInt(value);

                        if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                            foundTarget = true;
                            players.sendMessage(messageMe);
                            player.sendMessage(messageMe);
                        }
                    } else {
                        players.sendMessage(messageMe);
                        foundTarget = true;
                    }
                }
                else if (commandName.equalsIgnoreCase("gme")) {
                    boolean globalEnabled = enable.check("global-commands.me.enable");

                    if (globalEnabled) {
                        String globalCmd = config.getString("global-commands.me.message");
                        globalCmd = ReplaceUtils.replace(globalCmd, "%player%", player.getName(), "%action%", action);
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