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

public class DoCmd implements CommandExecutor {

    private final FileConfiguration config;

    public DoCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        EnableUtils enable = new EnableUtils(config);
        boolean result = enable.check("commands.do.enable");

        if (!result) {
            return true;
        }

        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) {
            return errorUtils.showError(sender, "player-only");
        }

        if (args.length >= 1) {
            String action = String.join(" ", args);
            boolean resultRange = enable.check("commands.do.enable");

            boolean foundTarget = false;

            Player player = (Player) sender;
            World world = player.getWorld();

            String messageDo = config.getString("commands.do.message");

            if (messageDo == null) {
                return nullError(sender, "commands.do.message");
            }

            messageDo = ReplaceUtils.replace(messageDo, "%player%", player.getName(), "%action%", action);
            messageDo = ChatColor.translateAlternateColorCodes('&', messageDo);

            String commandName = command.getName();

            for (Player players : world.getPlayers()) {
                if (commandName.equalsIgnoreCase("do")) {
                    if (resultRange) {
                        String value = config.getString("commands.do.range.range");

                        if (value == null) {
                            return nullError(sender, "commands.do.range.range");
                        }

                        int range = Integer.parseInt(value);

                        if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                            foundTarget = true;
                            players.sendMessage(messageDo);
                            player.sendMessage(messageDo);
                        }
                    } else {
                        players.sendMessage(messageDo);
                        foundTarget = true;
                    }
                }
                else if (commandName.equalsIgnoreCase("gdo")) {
                    boolean globalEnabled = enable.check("global-commands.do.enable");

                    if (globalEnabled) {
                        String globalCmd = config.getString("global-commands.do.message");
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