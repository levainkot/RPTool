package me.chelop.rptool.cmds;

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
        SendUtils sendUtils = new SendUtils(config);

        Player player = (Player) sender;
        String messageScream = config.getString("commands.scream.message", "%player% крикнул: %message%");
        String message = String.join(" ", args);

        messageScream = replace(messageScream, "%player%", player.getName(), "%message%", message);

        sendUtils.send(sender, command, args, messageScream, messageScream, false, "s", "scream", 15);

        return true;
    }
}