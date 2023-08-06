package me.chelop.rptool.cmds;

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SendUtils sendUtils = new SendUtils(config);

        Player player = (Player) sender;
        String messageTry = config.getString("commands.try.message", "&d%player% %action% &7| %result%");
        String globalMessage = config.getString("global-commands.try.message", "&7[&eG&7]&d%player% %action% &7| %result%");
        String action = String.join(" ", args);
        Random random = new Random();

        messageTry = replace(messageTry, "%player%", player.getName(), "%action%", action);
        globalMessage = replace(globalMessage, "%player%", player.getName(), "%action%", action);

        int randomValue = random.nextInt(101);
        String result = randomValue < 50 ? "&aУдачно" : "&cНеудачно";
        messageTry = replace(messageTry, "%result%", result);
        globalMessage = replace(globalMessage, "%result%", result);

        sendUtils.send(sender, command, args, messageTry, globalMessage, true, "try", "try", 15);

        return true;
    }
}