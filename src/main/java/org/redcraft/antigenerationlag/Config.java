package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    static public long timeFrameSeconds;

    static public int loadLimitPerSecond;

    static public int generationModifier;

    static public long rubberBandModifier;

    static public boolean warnPlayer;
    static public String warnMessage;

    static public boolean freezePlayer;
    static public long freezeDurationSeconds;
    static public long freezeCheckTickInterval;

    static public boolean debugSpeed;

    static public void readConfig(JavaPlugin plugin) {
        plugin.saveDefaultConfig();

        FileConfiguration config = plugin.getConfig();

        timeFrameSeconds = config.getLong("time-frame-seconds");

        loadLimitPerSecond = config.getInt("load-limit-per-second");

        generationModifier = config.getInt("generation-modifier");

        rubberBandModifier = config.getLong("rubber-band-modifier");

        warnPlayer = config.getBoolean("warn-player");
        warnMessage = config.getString("warn-message");

        freezePlayer = config.getBoolean("freeze-player");
        freezeDurationSeconds = config.getLong("freeze-duration-seconds");
        freezeCheckTickInterval = config.getLong("freeze-check-tick-interval");

        debugSpeed = config.getBoolean("debug-speed");
    }
}