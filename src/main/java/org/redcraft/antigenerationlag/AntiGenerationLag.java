package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.event.world.ChunkLoadEvent;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
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
		Chunk chunk = e.getChunk();

		Player nearestPlayer = this.getNearestPlayerFromChunk(chunk);

		if (nearestPlayer != null) {
			this.performPlayerSpeedChecks(nearestPlayer, chunk, e.isNewChunk());
		}
	}

	private void performPlayerSpeedChecks(Player player, Chunk chunk, boolean isNewChunk) {
		// TODO perform checks
	}

	private Player getNearestPlayerFromChunk(Chunk chunk) {
		Player nearestPlayer = null;
		double nearestPlayerDistance = Double.MAX_VALUE;

		Location centerChunkLocation = chunk.getBlock(8, 8, 128).getLocation();
		for (Player player : centerChunkLocation.getWorld().getPlayers()) {
			double playerDistanceFromChunk = centerChunkLocation.distance(player.getLocation());

			if (playerDistanceFromChunk < nearestPlayerDistance) {
				nearestPlayerDistance = playerDistanceFromChunk;
				nearestPlayer = player;
			}
		}

		return nearestPlayer;
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