package me.chelop.rptool.cmds;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

import static me.chelop.rptool.utils.ReplaceUtils.replace;

public class TodoCmd implements CommandExecutor {
    private final FileConfiguration config;

    public TodoCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!config.getBoolean("commands.todo.enable", true))
            return true;

        if (!(sender instanceof Player))
            return errorUtils.showError(sender, "player-only", "&cОшибка: Эта команда доступна только игроку");

        if (args.length < 1)
            return errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов.");

        String[] splitArgs = args[0].split("\\*");

        if (splitArgs.length < 2)
            return errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов.");

        String messageTodo = config.getString("commands.todo.message", "&d%action%, %player% сказал(а): &3%message%");
        String message = splitArgs[0];
        String action = splitArgs[1];
        Player player = (Player) sender;
        World world = player.getWorld();

        messageTodo = replace(messageTodo, "%player%", player.getName(), "%action%", action, "%message%", message);

        for (Player players : world.getPlayers()) {
            String commandName = command.getName();

            if (commandName.equalsIgnoreCase("todo")) {
                if (config.getBoolean("commands.todo.range.enable", true)) {
                    int range = config.getInt("commands.todo.range.range", 15);

                    if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                        players.sendMessage(messageTodo); player.sendMessage(messageTodo);
                        return true;
                    }
                } else {
                    players.sendMessage(messageTodo); player.sendMessage(messageTodo);
                    return true;
                }
            } else if (commandName.equalsIgnoreCase("gtodo")) {
                if (config.getBoolean("global-commands.todo.enable", true)) {
                    String globalCmd = config.getString("global-commands.todo.message", "&d%action%, %player% сказал(а): &3%message%");
                    globalCmd = replace(globalCmd, "%player%", player.getName(), "%action%", action, "%message%", message);

                    players.sendMessage(globalCmd); player.sendMessage(globalCmd);
                }
                return true;
            }
        }
        player.sendMessage(messageTodo);
        return errorUtils.showError(sender, "not-heard", "&eНикто не услышал тебя.");
    }
}