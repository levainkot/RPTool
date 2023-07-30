package me.chelop.rptool.cmds;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

import java.util.Random;

import static me.chelop.rptool.utils.ErrorUtils.nullError;

public class TryCmd implements CommandExecutor {
    private final FileConfiguration config;

    public TryCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        EnableUtils enable = new EnableUtils(config);
        boolean result = enable.check("commands.try.enable");

        if (!result) {
            return true;
        }

        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) return errorUtils.showError(sender, "player-only");

        if (args.length >= 1) {

            String action = String.join(" ", args);
            Random random = new Random();

            boolean resultRange = enable.check("commands.try.range.enable"); // Is the value from the "radius" configuration true?
            boolean foundTarget = false; // Variable, to check the radius

            Player player = (Player) sender;
            World world = player.getWorld(); // Get the world in which the sender is located

            String messageTry = config.getString("commands.try.message");

            if (messageTry == null) return nullError(sender, "commands.try.message");

            messageTry = ReplaceUtils.replace(messageTry, "%player%", player.getName(), "%action%", action);
            messageTry = ChatColor.translateAlternateColorCodes('&', messageTry);

            int randomValue = random.nextInt(11);

            String commandName = command.getName();

            for (Player players : world.getPlayers()) {
                if (commandName.equalsIgnoreCase("try")) {
                    if (resultRange) {
                        String value = config.getString("commands.try.range.range");

                        if (value == null) return nullError(sender, "commands.try.range.range");

                        if (randomValue < 5) {

                            if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < Integer.parseInt(value)) {
                                messageTry = messageTry.replaceAll("%result%", ChatColor.GREEN + "Удачно");

                                foundTarget = true;
                                players.sendMessage(messageTry);
                                player.sendMessage(messageTry);
                            }
                        } else {

                            if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < Integer.parseInt(value)) {
                                messageTry = messageTry.replaceAll("%result%", ChatColor.RED + "Неудачно");

                                foundTarget = true;
                                players.sendMessage(messageTry);
                                player.sendMessage(messageTry);
                            }
                        }
                    }
                    else {
                        if (randomValue < 5) {
                            messageTry = messageTry.replaceAll("%result%", ChatColor.GREEN + "Удачно");
                        }
                        else {
                            messageTry = messageTry.replaceAll("%result%", ChatColor.RED + "Неудачно");
                        }
                        players.sendMessage(messageTry);
                        foundTarget = true;
                    }
                }
                else if (commandName.equalsIgnoreCase("gtry")) {
                    boolean globalEnabled = enable.check("global-commands.try.enable");

                    if (globalEnabled) {
                        String globalCmd = config.getString("global-commands.try.message");
                        globalCmd = ReplaceUtils.replace(globalCmd, "%player%", player.getName(), "%action%", action);
                        globalCmd = ChatColor.translateAlternateColorCodes('&', globalCmd);

                        if (randomValue <= 5) {
                            globalCmd = globalCmd.replaceAll("%result%", ChatColor.GREEN + "Удачно");
                        } else {
                            globalCmd = globalCmd.replaceAll("%result%", ChatColor.RED + "Неудачно");
                        }
                        players.sendMessage(globalCmd);
                        foundTarget = true;
                    }
                    else {
                        return true;
                    }
                }
            }
            if (!foundTarget) return errorUtils.showError(sender, "not-heard");

        } else {
            return errorUtils.showError(sender, "not-enough-arguments");
        }
        return true;
    }
}
