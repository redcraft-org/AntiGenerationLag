package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.ChatColor;

public class Config {
    public int timeFrameSeconds;

    public int generationLimit;
    public int loadLimit;

    public boolean warnPlayer;
    public String warnMessage;

    public void readConfig(JavaPlugin plugin) {
        plugin.saveDefaultConfig();

        FileConfiguration config = plugin.getConfig();

        timeFrameSeconds = config.getInt("time-frame-seconds");

        generationLimit = config.getInt("generation-limit");

        loadLimit = config.getInt("load-limit");

        warnPlayer = config.getBoolean("warn-player");

        warnMessage = config.getString("warn-message");
        warnMessage = ChatColor.translateAlternateColorCodes('&', warnMessage);
    }
}