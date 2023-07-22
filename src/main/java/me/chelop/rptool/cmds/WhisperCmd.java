package me.chelop.rptool.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.ErrorUtils;
import me.chelop.rptool.utils.ReplaceUtils;

import static me.chelop.rptool.utils.ErrorUtils.nullError;

public class WhisperCmd implements CommandExecutor {

    private final FileConfiguration config;

    public WhisperCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) { // Checking whether the sender is equal to the player
            return errorUtils.showError(sender, "player-only");
        }

        if (!sender.hasPermission("rt.w")) { // Checking if the sender has permissions
            return errorUtils.showError(sender, "no-permissions");
        }

        Player player = (Player) sender;
        Player recipient = player.getServer().getPlayer(args[0]); // Get a player from 0 argument

        if (recipient == null) { // Checking if the recipient is in the game now
            return errorUtils.showError(sender, "whisper.player-not-found");
        }

        if (player == recipient) { // Checking whether the recipient is equal to the sender
            return errorUtils.showError(sender, "whisper.whisper-yourself");
        }

        if (args.length > 1) {
            StringBuilder message = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }

            // Processing the value "whisper.you-whispered"
            String youWhispered = config.getString("whisper.you-whispered"); // Get the value from the config

            if (youWhispered == null) {
                return nullError(sender, "whisper.you-whispered");
            }
            youWhispered = ReplaceUtils.replace(youWhispered, "%player%", player.getName(), "%message%", message);
            youWhispered = ChatColor.translateAlternateColorCodes('&', youWhispered);

            // Processing the value "whisper.whispered-you"
            String whisperedYou = config.getString("whisper.whispered-you"); // Get the value from the config

            if (whisperedYou == null) return nullError(sender, "whisper.whispered-you");
            whisperedYou = ReplaceUtils.replace(whisperedYou, "%player%", player.getName(), "%message%", message);
            whisperedYou = ChatColor.translateAlternateColorCodes('&', whisperedYou);

            // Sending the result to the sender
            String whisperMessage = youWhispered;
            player.sendMessage(whisperMessage);

            // Sending the result to the recipient
            String receiveMessage = whisperedYou;
            recipient.sendMessage(receiveMessage);
        }
        else return errorUtils.showError(sender, "not-enough-arguments");

        return true;
    }

}
