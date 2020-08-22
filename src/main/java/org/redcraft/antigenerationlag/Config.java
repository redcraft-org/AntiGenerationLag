package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public double timeFrameSeconds;

    public int loadLimit;
    public int generationModifier;

    public double rubberBandModifier;

    public boolean warnPlayer;
    public String warnMessage;
    public double warnCooldown;

    public void readConfig(JavaPlugin plugin) {
        plugin.saveDefaultConfig();

        FileConfiguration config = plugin.getConfig();

        timeFrameSeconds = config.getDouble("time-frame-seconds");

        loadLimit = config.getInt("load-limit");

        generationModifier = config.getInt("generation-modifier");

        rubberBandModifier = config.getDouble("rubber-band-modifier");

        warnPlayer = config.getBoolean("warn-player");
        warnMessage = config.getString("warn-message");
        warnCooldown = config.getDouble("warn-cooldown-seconds");
    }
}