package me.jefferyt.simpleSeen;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class PlayerDataStorage {
    private final File dataFile;
    private FileConfiguration config;

    public PlayerDataStorage() {
        dataFile = new File("plugins/SimpleSeen/players.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs(); // Ensure the directory exists
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadConfig();
    }

    private void loadConfig() {
        try {
            config = YamlConfiguration.loadConfiguration(dataFile);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Invalid YAML format in players.yml. Loading default empty configuration.");
            config = new YamlConfiguration(); // Load an empty configuration
        }
    }

    public void savePlayerLastSeen(UUID playerUUID, String playerName, long lastSeenTime) {
        try {
            config.set("players." + playerUUID.toString() + ".lastSeen", lastSeenTime);
            config.set("players." + playerUUID.toString() + ".lastKnownName", playerName);
            config.save(dataFile); // Save changes to the file
        } catch (IOException e) {
            System.err.println("Error saving players.yml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Long getPlayerLastSeen(UUID playerUUID) {
        try {
            return config.getLong("players." + playerUUID.toString() + ".lastSeen", -1L);
        } catch (Exception e) {
            System.err.println("Error reading last seen time for player " + playerUUID + ": " + e.getMessage());
            return -1L; // Fallback value if an error occurs
        }
    }

    public Map<UUID, Long> getAllPlayerLastSeen() {
        Map<UUID, Long> playerLastSeenMap = new HashMap<>();
        try {
            if (config.contains("players")) {
                for (String uuidStr : config.getConfigurationSection("players").getKeys(false)) {
                    try {
                        UUID playerUUID = UUID.fromString(uuidStr); // Validate UUID format
                        long lastSeenTime = config.getLong("players." + uuidStr + ".lastSeen", -1L);
                        playerLastSeenMap.put(playerUUID, lastSeenTime);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid UUID format in players.yml: " + uuidStr);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading all player data from players.yml: " + e.getMessage());
        }
        return playerLastSeenMap;
    }

    public UUID[] getAllPlayerUUIDs() {
        Map<UUID, Long> playerLastSeenMap = getAllPlayerLastSeen();
        return playerLastSeenMap.keySet().toArray(new UUID[0]);
    }

    public String[] getAllKnownNames() {
        LinkedList<String> knownNames = new LinkedList<String>();
        try
        {
            if (config.contains("players")) {
                for (String uuidStr : config.getConfigurationSection("players").getKeys(false)) {
                    try {
                        String lastKnownName = config.getString("players." + uuidStr + ".lastKnownName");
                        knownNames.add(lastKnownName);
                    } catch (Exception e) {
                        System.err.println("Error retrieving player names in players.yml");
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println("Error reading all player data from players.yml: " + e.getMessage());
        }
        return knownNames.toArray(new String[0]);
    }

}
