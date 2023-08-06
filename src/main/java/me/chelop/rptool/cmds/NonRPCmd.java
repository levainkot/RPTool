package me.chelop.rptool.cmds;

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
        SendUtils sendUtils = new SendUtils(config);

        Player player = (Player) sender;
        String messageNrp = config.getString("commands.nrp.message", "(( %player% %message% ))");
        String globalMessage = config.getString("global-commands.nrp.message", "&7[&eG&7] (( %player% %message% ))");
        String message = String.join(" ", args);

        messageNrp = replace(messageNrp, "%player%", player.getName(), "%message%", message);
        globalMessage = replace(globalMessage, "%player%", player.getName(), "%message%", message);

        sendUtils.send(sender, command, args, messageNrp, globalMessage, true, "n", "nrp", 15);

        return true;
    }
}