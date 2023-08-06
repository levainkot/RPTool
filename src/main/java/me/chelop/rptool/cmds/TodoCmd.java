package me.chelop.rptool.cmds;

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
        SendUtils sendUtils = new SendUtils(config);
        ErrorUtils errorUtils = new ErrorUtils(config);

        String[] splitArgs = args[0].split("\\*");

        if (splitArgs.length < 2)
            errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов.");

        String messageTodo = config.getString("commands.todo.message", "&d%action%, %player% сказал(а): &3%message%");
        String globalMessage = config.getString("global-commands.todo.message", "&7[&eG&7] &d%action%, %player% сказал(а): &3%message%");
        String message = splitArgs[0];
        String action = splitArgs[1];
        Player player = (Player) sender;

        messageTodo = replace(messageTodo, "%player%", player.getName(), "%action%", action, "%message%", message);
        globalMessage = replace(globalMessage, "%player%", player.getName(), "%action%", action, "%message%", message);

        sendUtils.send(sender, command, args, messageTodo, globalMessage, true, "todo", "todo", 15);

        return true;
    }
}