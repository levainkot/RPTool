package me.chelop.rptool.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Completer implements TabCompleter {

    private final List<String> actionCommands = Arrays.asList("me", "do", "try", "gme", "gdo", "gtry");
    private final List<String> messageCommands = Arrays.asList("n", "s", "gn");
    private final List<String> todoCommands = Arrays.asList("todo", "gtodo");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        String commandName = command.getName().toLowerCase();

        if (actionCommands.contains(commandName) && args.length == 1) {
            completions.add("действие");
        } else if (messageCommands.contains(commandName) && args.length == 1) {
            completions.add("сообщение");
        } else if (todoCommands.contains(commandName) && args.length == 1) {
            completions.add("сообщение*действие");
        } else if (command.getName().equalsIgnoreCase("rt") && args.length == 1) {
            completions.add("reload");
        } else if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            Player player = (Player) sender;

            for (Player players : player.getWorld().getPlayers()) {
                playerNames.add(players.getName());
            }
            return playerNames;
        } else if (args.length == 2) {
            completions.add("сообщение");
        }

        return completions;
    }
}