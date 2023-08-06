package me.chelop.rptool.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

import static me.chelop.rptool.utils.ReplaceUtils.replace;

public class MeCmd implements CommandExecutor {

    private final FileConfiguration config;

    public MeCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SendUtils sendUtils = new SendUtils(config);

        Player player = (Player) sender;
        String messageMe = config.getString("commands.me.message", "&d%player% %action%");
        String globalMessage = config.getString("global-commands.me.message", "&7[&eG&7] &d%player% %action%");
        String action = String.join(" ", args);

        messageMe = replace(messageMe, "%player%", player.getName(), "%action%", action);
        globalMessage = replace(globalMessage, "%player%", player.getName(), "%action%", action);

        sendUtils.send(sender, command, args, messageMe, globalMessage, true, "me", "me", 15);

        return true;
    }
}