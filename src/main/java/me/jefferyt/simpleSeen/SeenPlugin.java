package me.jefferyt.simpleSeen;

import me.jefferyt.simpleSeen.commands.*;

import me.jefferyt.simpleSeen.listeners.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.UUID;

public class SeenPlugin extends JavaPlugin implements Listener {

    private PlayerDataStorage playerDataStorage;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        playerDataStorage = new PlayerDataStorage(); // Instantiate PlayerDataStorage

        // Register commands
        CommandRegisterer commandRegisterer = new CommandRegisterer(this);
        commandRegisterer.registerCommand("seen", new SeenCommand(playerDataStorage));

        // Register the tab completer for the /seen command
        getCommand("seen").setTabCompleter(new SeenTabCompleter(playerDataStorage));

        // Register the event listener
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(playerDataStorage), this);

        getLogger().info("LastSeenPlugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("LastSeenPlugin has been disabled.");
    }

}
