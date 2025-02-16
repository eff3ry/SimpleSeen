package me.jefferyt.simpleSeen;


import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegisterer {

    private final JavaPlugin plugin;

    public CommandRegisterer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommand(String commandName, CommandExecutor executor) {
        plugin.getCommand(commandName).setExecutor(executor);
    }
}
