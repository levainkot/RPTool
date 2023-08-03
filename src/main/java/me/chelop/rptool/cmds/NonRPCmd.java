package me.chelop.rptool.cmds;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

import static me.chelop.rptool.utils.ReplaceUtils.replace;

public class NonRPCmd implements CommandExecutor {

    private final FileConfiguration config;

    public NonRPCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!config.getBoolean("commands.nrp.enable", true))
            return true;

        if (!(sender instanceof Player))
            return errorUtils.showError(sender, "player-only", "&cОшибка: Можно использовать только игроку.");

        if (args.length < 1)
            return errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов.");

        Player player = (Player) sender;
        World world = player.getWorld();
        String messageNrp = config.getString("commands.nrp.message", "%player% %message%");
        String message = String.join(" ", args);

        messageNrp = replace(messageNrp, "%player%", player.getName(), "%message%", message);

        for (Player players : world.getPlayers()) {
            String commandName = command.getName();

            if (commandName.equalsIgnoreCase("n")) {
                if (config.getBoolean("commands.nrp.range.enable", true)) {
                    int range = config.getInt("commands.nrp.range.range", 15);

                    if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                        players.sendMessage(messageNrp); player.sendMessage(messageNrp);
                        return true;
                    }
                } else {
                    players.sendMessage(messageNrp); player.sendMessage(messageNrp);
                    return true;
                }
            } else if (commandName.equalsIgnoreCase("gn")) {
                if (config.getBoolean("global-commands.nrp.enable", true)) {
                    String globalCmd = config.getString("global-commands.nrp.message", "&d%player% %message%");
                    globalCmd = replace(globalCmd, "%player%", player.getName(), "%message%", message);

                    players.sendMessage(globalCmd); player.sendMessage(globalCmd);
                }
                return true;
            }
        }
        player.sendMessage(messageNrp);
        return errorUtils.showError(sender, "not-heard", "&eНикто не услышал тебя.");
    }
}