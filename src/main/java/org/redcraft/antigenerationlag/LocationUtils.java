package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationUtils {

    HashMap<UUID, double[]> chunkTimestamps = new HashMap<UUID, double[]>();

    Config config = new Config();

    public LocationUtils(JavaPlugin plugin) {
        config.readConfig(plugin);
    }

    public void performPlayerSpeedChecks(Player player, Chunk chunk, boolean isNewChunk) {
        double[] lastLoadedChunks = chunkTimestamps.getOrDefault(player.getUniqueId(), new double[] {});

        // TODO Do the checks and rubber band player

        chunkTimestamps.put(player.getUniqueId(), lastLoadedChunks);
    }

    public Player getNearestPlayerFromChunk(Chunk chunk) {
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
}