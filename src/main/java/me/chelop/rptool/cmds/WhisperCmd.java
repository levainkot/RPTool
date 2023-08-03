package me.chelop.rptool.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.chelop.rptool.utils.*;

public class WhisperCmd implements CommandExecutor {

    private final FileConfiguration config;

    public WhisperCmd(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ErrorUtils errorUtils = new ErrorUtils(config);

        if (!config.getBoolean("commands.whisper.enable", true))
            return true;

        if (!(sender instanceof Player))
            return errorUtils.showError(sender, "player-only", "&cОшибка: Эта команда доступна только игроку");

        if (args.length < 2)
            return errorUtils.showError(sender, "not-enough-arguments", "&cОшибка: Недостаточно аргументов");

        Player player = (Player) sender;
        Player recipient = player.getServer().getPlayer(args[0]);

        if (recipient == null)
            return errorUtils.showError(sender, "commands.whisper.player-not-found", "Ошибка: Игрок не найден");

        if (player == recipient)
            return errorUtils.showError(sender, "commands.whisper.whisper-yourself", "&cОшибка: Нельзя шептать самому себе.");

        if (config.getBoolean("commands.whisper.distance-between-players.enable", true)) {
            String distance = config.getString("commands.whisper.distance-between-players.distance", "5");

            double maxDistance = Double.parseDouble(distance);

            if (player.getLocation().distance(recipient.getLocation()) > maxDistance)
                return errorUtils.showError(sender, "commands.whisper.distance-exceeded", "&cСлишком большое расстояние между вами и получателем.");
        }

        String youWhispered = config.getString("commands.whisper.you-whispered", "&7Ты прошептал %player%: %message%");
        String whisperedYou = config.getString("commands.whisper.whispered-you", "&7%player% прошептал тебе: %message%");

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        youWhispered = ReplaceUtils.replace(youWhispered, "%player%", recipient.getName(), "%message%", message);
        whisperedYou = ReplaceUtils.replace(whisperedYou, "%player%", player.getName(), "%message%", message);

        player.sendMessage(youWhispered);
        recipient.sendMessage(whisperedYou);

        return true;
    }

}