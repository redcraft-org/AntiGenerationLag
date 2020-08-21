package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.event.world.ChunkLoadEvent;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class AntiGenerationLag extends JavaPlugin implements Listener {

	LocationUtils lu = new LocationUtils(this);

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onChunkLoad(ChunkLoadEvent e) {
		Chunk chunk = e.getChunk();

		Player nearestPlayer = lu.getNearestPlayerFromChunk(chunk);

		if (nearestPlayer != null) {
			lu.performPlayerSpeedChecks(nearestPlayer, chunk, e.isNewChunk());
		}
	}
}