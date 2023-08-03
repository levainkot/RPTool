package me.chelop.rptool.cmds;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

import static me.chelop.rptool.utils.ReplaceUtils.replace;

public class DoCmd implements CommandExecutor {

    private final FileConfiguration config;

    public DoCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!config.getBoolean("commands.do.enable", true))
            return true;

        if (!(sender instanceof Player))
            return errorUtils.showError(sender, "player-only", "&cОшибка: Можно использовать только игроку.");

        if (args.length < 1)
            return errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов.");

        Player player = (Player) sender;
        World world = player.getWorld();
        String messageMe = config.getString("commands.do.message", "&d%player% %action%");
        String action = String.join(" ", args);

        messageMe = replace(messageMe, "%player%", player.getName(), "%action%", action);

        for (Player players : world.getPlayers()) {
            String commandName = command.getName();

            if (commandName.equalsIgnoreCase("do")) {
                if (config.getBoolean("commands.do.range.enable", true)) {
                    int range = config.getInt("commands.do.range.range", 15);

                    if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                        players.sendMessage(messageMe); player.sendMessage(messageMe);
                        return true;
                    }
                } else {
                    players.sendMessage(messageMe); player.sendMessage(messageMe);
                    return true;
                }
            } else if (commandName.equalsIgnoreCase("gdo")) {
                if (config.getBoolean("global-commands.do.enable", true)) {
                    String globalCmd = config.getString("global-commands.do.message", "&d%player% %action%");
                    globalCmd = replace(globalCmd, "%player%", player.getName(), "%action%", action);

                    players.sendMessage(globalCmd); player.sendMessage(globalCmd);
                }
                return true;
            }
        }
        player.sendMessage(messageMe);
        return errorUtils.showError(sender, "not-heard", "&eНикто не услышал тебя.");
    }
}