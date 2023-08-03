package me.chelop.rptool.listeners;

import me.chelop.rptool.utils.ErrorUtils;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static me.chelop.rptool.utils.ReplaceUtils.replace;

public class SmileListener implements Listener {
    private final FileConfiguration config;

    public SmileListener(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        ErrorUtils errorUtils = new ErrorUtils(config);

        World world = player.getWorld();

        String formattedMessage = null;
        switch (message) {
            case "))":
                formattedMessage = getFormattedMessage(player, "смеётся");
                break;
            case ")":
                formattedMessage = getFormattedMessage(player, "улыбается");
                break;
            case "((":
                formattedMessage = getFormattedMessage(player, "очень расстроился");
                break;
            case "(":
                formattedMessage = getFormattedMessage(player, "расстроился");
                break;
        }
        if (formattedMessage == null)
            return;

        boolean rangeEnable = config.getBoolean("commands.me.range.enable");

        for (Player players : world.getPlayers()) {
            if (rangeEnable) {
                int range = config.getInt("commands.me.range.range", 15);

                if (!player.equals(players) && player.getLocation().distance(players.getLocation()) < range) {
                    players.sendMessage(formattedMessage);

                    event.setCancelled(true);
                    return;
                }
            } else {
                players.sendMessage(formattedMessage);

                event.setCancelled(true);
                return;
            }
        }
        player.sendMessage(formattedMessage);
        errorUtils.showError(player, "not-heard", "&eНикто тебя не услышал.");

        event.setCancelled(true);
    }

    private String getFormattedMessage(Player player, String action) {
        String message = config.getString("commands.me.message");
        return replace(message, "%player%", player.getName(), "%action%", action);
    }
}