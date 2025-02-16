package me.jefferyt.simpleSeen.commands;

import me.jefferyt.simpleSeen.PlayerDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SeenTabCompleter implements TabCompleter {

    private final PlayerDataStorage playerDataStorage;

    public SeenTabCompleter(PlayerDataStorage playerDataStorage) {
        this.playerDataStorage = playerDataStorage;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        // Ensure the sender is a player and that the command is for "/seen"
        if (args.length == 1 && sender instanceof Player) {
            String partialName = args[0].toLowerCase();

            // Check if the server is running in online mode
            boolean isOnlineMode = Bukkit.getServer().getOnlineMode();

            // Get all stored UUIDs and check if they match the name pattern
            if (isOnlineMode) {
                for (UUID uuid : playerDataStorage.getAllPlayerUUIDs()) {
                    String playerName = Bukkit.getOfflinePlayer(uuid).getName();
                    if (playerName != null && playerName.toLowerCase().startsWith(partialName)) {
                        suggestions.add(playerName);
                    }
                }
            } else {
                //offline server cant get names from generated uuid so use last known playernames
                for (String playerName : playerDataStorage.getAllKnownNames())
                {
                    if (playerName != null && playerName.toLowerCase().startsWith(partialName)) {
                        suggestions.add(playerName);
                    }
                }
            }
        }

        return suggestions;
    }
}
