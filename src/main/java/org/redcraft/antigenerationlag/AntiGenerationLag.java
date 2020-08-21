package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AntiGenerationLag extends JavaPlugin implements Listener {
	int timeFrameSeconds;

	int generationLimit;
	int loadLimit;

	boolean warnPlayer;
	String warnMessage;

	@Override
	public void onEnable() {
		readConfig();
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onChunkLoad(ChunkLoadEvent e) {
		// TODO check chunk load events
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onChunkGenerate(ChunkPopulateEvent e) {
		// TODO check chunk load events
	}

	private void readConfig() {
		saveDefaultConfig();

		timeFrameSeconds = getConfig().getInt("time-frame-seconds");

		generationLimit = getConfig().getInt("generation-limit");

		loadLimit = getConfig().getInt("load-limit");

		warnPlayer = getConfig().getBoolean("warn-player");

		warnMessage = getConfig().getString("warn-message");
		warnMessage = ChatColor.translateAlternateColorCodes('&', warnMessage);
	}
}