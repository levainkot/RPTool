package me.chelop.rptool.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import me.chelop.rptool.utils.*;

import static me.chelop.rptool.utils.ErrorUtils.nullError;

public class WhisperCmd implements CommandExecutor {

    private final FileConfiguration config;

    public WhisperCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        EnableUtils enable = new EnableUtils(config);
        boolean result = enable.check("commands.whisper.enable");

        if (!result) {
            return true;
        }

        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!(sender instanceof Player)) { // Checking whether the sender is equal to the player
            return errorUtils.showError(sender, "player-only");
        }

        Player player = (Player) sender;
        Player recipient = player.getServer().getPlayer(args[0]); // Get a player from 0 argument

        if (recipient == null) { // Checking if the recipient is in the game now
            return errorUtils.showError(sender, "commands.whisper.player-not-found");
        }

        if (player == recipient) { // Checking whether the recipient is equal to the sender
            return errorUtils.showError(sender, "commands.whisper.whisper-yourself");
        }

        if (args.length < 2)
            return errorUtils.showError(sender, "not-enough-arguments");

        boolean resultDistance = enable.check("commands.whisper.distance-between-players.enable");

        if (resultDistance) {
            // Checking the distance between sender and recipient
            Location senderLocation = player.getLocation();
            Location recipientLocation = recipient.getLocation();
            double distance = senderLocation.distance(recipientLocation);
            String number = config.getString("commands.whisper.distance-between-players.distance");

            if (number != null) {
                double maxDistance = Double.parseDouble(number);

                if (distance > maxDistance) {
                    return errorUtils.showError(sender, "commands.whisper.distance-exceeded");
                }
            } else {
                return nullError(sender, "commands.whisper.distance-between-players.distance");
            }
        }

        // Processing the value "whisper.you-whispered"
        String youWhispered = config.getString("commands.whisper.you-whispered"); // Get the value from the config

        if (youWhispered == null) {
            return nullError(sender, "commands.whisper.you-whispered");
        }
        StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i++)
            message.append(args[i]).append(" ");

        youWhispered = ReplaceUtils.replace(youWhispered, "%player%", recipient.getName(), "%message%", message);
        youWhispered = ChatColor.translateAlternateColorCodes('&', youWhispered);

        // Processing the value "whisper.whispered-you"
        String whisperedYou = config.getString("commands.whisper.whispered-you"); // Get the value from the config

        if (whisperedYou == null) return nullError(sender, "commands.whisper.whispered-you");
        whisperedYou = ReplaceUtils.replace(whisperedYou, "%player%", player.getName(), "%message%", message);
        whisperedYou = ChatColor.translateAlternateColorCodes('&', whisperedYou);

        // Sending the result to the sender
        String whisperMessage = youWhispered;
        player.sendMessage(whisperMessage);

        // Sending the result to the recipient
        String receiveMessage = whisperedYou;
        recipient.sendMessage(receiveMessage);

        return true;
    }

}