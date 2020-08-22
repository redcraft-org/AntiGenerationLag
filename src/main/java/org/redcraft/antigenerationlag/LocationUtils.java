package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationUtils {

    HashMap<UUID, ArrayList<Double>> chunkTimestamps = new HashMap<UUID, ArrayList<Double>>();

    HashMap<UUID, Double> warnCooldowns = new HashMap<UUID, Double>();

    Config config = new Config();

    public LocationUtils(JavaPlugin plugin) {
        config.readConfig(plugin);
    }

    public void performPlayerSpeedChecks(Player player, Chunk chunk, boolean isNewChunk) {
        UUID playerUniqueId = player.getUniqueId();

        double currentTimestamp = System.currentTimeMillis();
        double timestampThreshold = currentTimestamp - config.timeFrameSeconds * 1000;

        ArrayList<Double> lastLoadedChunks = chunkTimestamps.getOrDefault(playerUniqueId, new ArrayList<Double>());

        // Filter loaded chunks that are older than the defined amount of seconds in the config
        lastLoadedChunks.removeIf(timestamp -> timestamp > timestampThreshold);

        int chunksToAdd = isNewChunk ? config.generationModifier : 1;

        for (int i = 0; i < chunksToAdd; i++) {
            lastLoadedChunks.add(currentTimestamp);
        }

        chunkTimestamps.put(playerUniqueId, lastLoadedChunks);

        if (lastLoadedChunks.size() > config.loadLimit) {
            Vector rubberBandVelocity = player.getVelocity().multiply(-1 * config.rubberBandModifier);
            Location rubberBandLocation = player.getLocation().add(rubberBandVelocity);
            this.punishPlayer(player, rubberBandLocation);
        }
    }

    public void punishPlayer(Player player, Location rubberBandLocation) {
        player.teleport(rubberBandLocation);
        player.setVelocity(new Vector(0, 0, 0));

        if (config.warnPlayer) {
            UUID playerUniqueId = player.getUniqueId();
            double warnCooldown = warnCooldowns.getOrDefault(playerUniqueId, 0.0);

            double currentTimestamp = System.currentTimeMillis();

            if (warnCooldown < currentTimestamp) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.warnMessage));
                warnCooldowns.put(playerUniqueId, currentTimestamp + (config.warnCooldown * 1000));
            }
        }
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