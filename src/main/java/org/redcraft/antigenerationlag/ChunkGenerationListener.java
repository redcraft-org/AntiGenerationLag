package org.redcraft.antigenerationlag;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkGenerationListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
    public void onChunkLoad(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();

        Player nearestPlayer = LocationSpeedUtils.getNearestPlayerFromChunk(chunk);

        if (nearestPlayer != null) {
            LocationSpeedUtils.performPlayerSpeedChecks(nearestPlayer, chunk, e.isNewChunk());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent e) {
        LocationSpeedUtils.resetPlayerTimestamps(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        LocationSpeedUtils.resetPlayerTimestamps(e.getPlayer());
    }
}