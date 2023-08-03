package me.chelop.rptool.cmds;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

import java.util.Random;

import static me.chelop.rptool.utils.ReplaceUtils.replace;

public class TryCmd implements CommandExecutor {
    private final FileConfiguration config;

    public TryCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!config.getBoolean("commands.try.enable", true))
            return true;

        if (!(sender instanceof Player))
            return errorUtils.showError(sender, "player-only", "&cОшибка: Эта команда доступна только игроку");

        if (args.length < 1)
            return errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов");

        String action = String.join(" ", args);
        Random random = new Random();
        Player player = (Player) sender;
        World world = player.getWorld();

        String messageTry = config.getString("commands.try.message", "&d%player% %action% &7| %result%");

        for (Player players : world.getPlayers()) {
            int randomValue = random.nextInt(101);

            if (randomValue < 50)
                messageTry = replace(messageTry, "%result%", "&aУдачно", "%player%", player.getName(), "%action%", action);
            else
                messageTry = replace(messageTry, "%result%", "&cНеудачно", "%player%", player.getName(), "%action%", action);

            String commandName = command.getName();

            if (commandName.equalsIgnoreCase("try")) {
                if (config.getBoolean("commands.try.range.enable", true)) {
                    int range = config.getInt("commands.try.range.range", 15);

                    if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                        players.sendMessage(messageTry); player.sendMessage(messageTry);
                        return true;
                    }
                }
                else {
                    players.sendMessage(messageTry); player.sendMessage(messageTry);
                    return true;
                }
            }
            else if (commandName.equalsIgnoreCase("gtry")) {
                if (config.getBoolean("global-commands.try.enable", true)) {
                    String globalCmd = config.getString("global-commands.try.message", "&d%player% %action% &7| %result%");

                    if (randomValue < 50)
                        globalCmd = replace(globalCmd, "%result%", "&aУдачно", "%player%", player.getName(), "%action%", action);
                    else
                        globalCmd = replace(globalCmd, "%result%", "&cНеудачно", "%player%", player.getName(), "%action%", action);

                    players.sendMessage(globalCmd); player.sendMessage(globalCmd);
                }
                return true;
            }
        }
        player.sendMessage(messageTry);
        return errorUtils.showError(sender, "not-heard", "&eНикто не услышал тебя.");
    }
}
