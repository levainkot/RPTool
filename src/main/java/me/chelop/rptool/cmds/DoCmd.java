package me.chelop.rptool.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.SendUtils;

import static me.chelop.rptool.utils.ReplaceUtils.replace;

public class DoCmd implements CommandExecutor {

    private final FileConfiguration config;

    public DoCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SendUtils sendUtils = new SendUtils(config);

        Player player = (Player) sender;
        String messageDo = config.getString("commands.do.message", "&d%action% %player%");
        String globalMessage = config.getString("global-commands.do.message", "&7[&eG&7] &d%action% %player%");
        String action = String.join(" ", args);

        messageDo = replace(messageDo, "%player%", player.getName(), "%action%", action);
        globalMessage = replace(globalMessage, "%player%", player.getName(), "%action%", action);

        sendUtils.send(sender, command, args, messageDo, globalMessage, true, "do", "do", 15);

        return true;
    }
}