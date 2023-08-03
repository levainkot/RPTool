package me.chelop.rptool.cmds;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

import static me.chelop.rptool.utils.ReplaceUtils.replace;

public class ScreamCmd implements CommandExecutor {

    private final FileConfiguration config;

    public ScreamCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!config.getBoolean("commands.scream.enable", true))
            return true;

        if (!(sender instanceof Player))
            return errorUtils.showError(sender, "player-only", "&cОшибка: Можно использовать только игроку.");

        if (args.length < 1)
            return errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов.");

        String message = String.join(" ", args);
        Player player = (Player) sender;
        World world = player.getWorld();
        String messageScream = config.getString("commands.scream.message", "%player% крикнул: %message%");

        messageScream = replace(messageScream, "%player%", player.getName(), "%message%", message);

        for (Player players : world.getPlayers()) {
            if (config.getBoolean("commands.scream.range.enable", true)) {
                int range = config.getInt("commands.scream.range.range", 30);

                if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                    players.sendMessage(messageScream); player.sendMessage(messageScream);
                    return true;
                }
            } else {
                players.sendMessage(messageScream); player.sendMessage(messageScream);
                return true;
            }
        }
        player.sendMessage(messageScream);
        return errorUtils.showError(sender, "not-heard", "&eНикто не услышал тебя.");
    }
}