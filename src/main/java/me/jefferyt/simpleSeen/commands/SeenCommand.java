package me.jefferyt.simpleSeen.commands;

import me.jefferyt.simpleSeen.PlayerDataStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.time.Duration;
import java.time.Instant;

public class SeenCommand implements CommandExecutor {

    private final PlayerDataStorage playerDataStorage;

    public SeenCommand(PlayerDataStorage playerDataStorage) {
        this.playerDataStorage = playerDataStorage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Component.text("Usage: /seen <player>").color(NamedTextColor.RED));
            return true;
        }

        String targetName = args[0];
        Player targetPlayer = Bukkit.getPlayerExact(targetName);

        // Check if the server is running in online mode
        boolean isOnlineMode = Bukkit.getServer().getOnlineMode();

        if (targetPlayer != null && targetPlayer.isOnline()) {
            sender.sendMessage(Component.text("<" + targetName + "> is currently online.").color(NamedTextColor.GREEN));
        } else {
            // Use the player's name or UUID depending on online mode
            UUID targetUUID = null;

            if (isOnlineMode) {
                // If in online mode, use UUID
                targetPlayer = Bukkit.getPlayer(targetName);
                if (targetPlayer != null) {
                    targetUUID = targetPlayer.getUniqueId();
                } else {
                    sender.sendMessage(Component.text("No such player found.").color(NamedTextColor.RED));
                    return true;
                }
            } else {
                // If offline mode, use player name (you can store by name or UUID in this case)
                targetUUID = UUID.nameUUIDFromBytes(targetName.getBytes()); // Generate a UUID based on the name in offline mode
            }

            if (targetUUID != null) {
                long lastSeenTime = playerDataStorage.getPlayerLastSeen(targetUUID); // Get last seen time from storage
                if (lastSeenTime != -1L) {
                    String lastSeenFormatted = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date(lastSeenTime));
                    sender.sendMessage(Component.text("<" + targetName + "> was last seen " + getRelativeTime(lastSeenTime)).color(NamedTextColor.YELLOW));
                } else {
                    sender.sendMessage(Component.text("No data found for <" + targetName + ">").color(NamedTextColor.RED));
                }
            }
        }
        return true;
    }

    public String getRelativeTime(long lastSeenTimestamp) {

        // Get the current time in UTC
        Instant now = Instant.now();

        // Convert the last seen timestamp (milliseconds) to an Instant
        Instant lastSeenTime = Instant.ofEpochMilli(lastSeenTimestamp);

        // Calculate the difference between now and the last seen time
        Duration duration = Duration.between(lastSeenTime, now);

        // Extract days, hours, minutes, and seconds from the duration
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        // Format the relative time string
        StringBuilder relativeTime = new StringBuilder();

        if (days > 0) {
            relativeTime.append(days).append("d ");
        }
        if (hours > 0) {
            relativeTime.append(hours).append("h ");
        }
        if (minutes > 0) {
            relativeTime.append(minutes).append("m ");
        }
        relativeTime.append(seconds).append("s ago");

        return relativeTime.toString();
    }
}
