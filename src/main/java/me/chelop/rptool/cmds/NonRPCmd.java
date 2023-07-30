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

public class NonRPCmd implements CommandExecutor {

    private final FileConfiguration config;

    public NonRPCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        EnableUtils enable = new EnableUtils(config);
        boolean result = enable.check("commands.nrp.enable");

        if (!result) {
            return true;
        }

        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) {
            return errorUtils.showError(sender, "player-only");
        }

        if (args.length >= 1) {
            String message = String.join(" ", args);

            boolean resultRange = enable.check("commands.nrp.range.enable");
            boolean foundTarget = false;

            Player player = (Player) sender;
            World world = player.getWorld();

            String messageNrp = config.getString("commands.nrp.message");

            if (messageNrp == null) {
                return nullError(sender, "commands.nrp.message");
            }

            messageNrp = ReplaceUtils.replace(messageNrp, "%player%", player.getName(), "%message%", message);
            messageNrp = ChatColor.translateAlternateColorCodes('&', messageNrp);

            String commandName = command.getName();
            boolean n = commandName.equalsIgnoreCase("n");
            boolean b = commandName.equalsIgnoreCase("b");

            for (Player players : world.getPlayers()) {
                if (n || b) {
                    if (resultRange) {
                        String value = config.getString("commands.nrp.range.range");

                        if (value == null) {
                            return nullError(sender, "commands.nrp.range.range");
                        }

                        int range = Integer.parseInt(value);

                        if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                            foundTarget = true;
                            players.sendMessage(messageNrp);
                        }
                    } else {
                        players.sendMessage(messageNrp);
                        foundTarget = true;
                    }
                } else if (commandName.equalsIgnoreCase("gn")) {
                    boolean globalEnabled = enable.check("global-commands.nrp.enable");

                    if (globalEnabled) {
                        String globalCmd = config.getString("global-commands.nrp.message");
                        globalCmd = ReplaceUtils.replace(globalCmd, "%player%", player.getName(), "%message%", message);
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