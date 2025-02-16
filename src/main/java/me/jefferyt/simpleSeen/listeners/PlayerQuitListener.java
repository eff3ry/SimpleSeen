package me.jefferyt.simpleSeen.listeners;

import me.jefferyt.simpleSeen.PlayerDataStorage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;
import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final PlayerDataStorage playerDataStorage;

    // Constructor to receive PlayerDataStorage instance
    public PlayerQuitListener(PlayerDataStorage playerDataStorage) {
        this.playerDataStorage = playerDataStorage;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        UUID playerUUID;

        // Check if the server is running in online mode
        boolean isOnlineMode = event.getPlayer().getServer().getOnlineMode();

        if (isOnlineMode) {
            // In online mode, use the player's actual UUID
            playerUUID = event.getPlayer().getUniqueId();
        } else {
            // In offline mode, generate a UUID based on the player's name
            playerUUID = UUID.nameUUIDFromBytes(playerName.getBytes());
        }

        // Save the last seen time using PlayerDataStorage
        playerDataStorage.savePlayerLastSeen(playerUUID, playerName, Instant.now().toEpochMilli());
    }
}
