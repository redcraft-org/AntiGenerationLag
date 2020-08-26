package org.redcraft.antigenerationlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.util.Vector;
import org.redcraft.antigenerationlag.models.FrozenPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class LocationSpeedUtils {

    static private HashMap<UUID, ArrayList<Long>> chunkTimestamps = new HashMap<UUID, ArrayList<Long>>();

    static private HashMap<UUID, FrozenPlayer> frozenPlayers = new HashMap<UUID, FrozenPlayer>();
    static private Lock frozenPlayersLock = new ReentrantLock(true);

    static public void checkFrozenPlayers() {
        // Checks for frozen players and teleport them back to their frozen point
        frozenPlayersLock.lock();
        try {
            for (Map.Entry<UUID, FrozenPlayer> entry : frozenPlayers.entrySet()) {
                UUID playerUniqueId = entry.getKey();
                FrozenPlayer frozenPlayer = entry.getValue();

                Player player = Bukkit.getServer().getPlayer(playerUniqueId);

                // If the player is online and they're frozen, teleport them back
                if (player.isOnline() && System.currentTimeMillis() < frozenPlayer.frozenUntil) {
                    player.teleport(frozenPlayer.location);
                } else {
                    // Or delete them from the frozen list if they're offline or no longer frozen
                    frozenPlayers.remove(playerUniqueId);
                }
            }
        } catch(Exception e) {
            Bukkit.getServer().getLogger().warning("An error occurred while checking frozen players: " + e.getLocalizedMessage());
        }
        frozenPlayersLock.unlock();
    }

    static public void performPlayerSpeedChecks(Player player, Chunk chunk, boolean isNewChunk) {
        UUID playerUniqueId = player.getUniqueId();

        long currentTimestamp = System.currentTimeMillis();
        long timestampThreshold = currentTimestamp - Config.timeFrameSeconds * 1000;

        ArrayList<Long> lastLoadedChunks = chunkTimestamps.getOrDefault(playerUniqueId, new ArrayList<Long>());

        // Filter loaded chunks that are older than the amount of seconds in the config
        lastLoadedChunks.removeIf(timestamp -> timestamp < timestampThreshold);

        int chunksToAdd = isNewChunk ? Config.generationModifier : 1;

        // Count chunks multiple times if they're new
        for (int i = 0; i < chunksToAdd; i++) {
            lastLoadedChunks.add(currentTimestamp);
        }

        chunkTimestamps.put(playerUniqueId, lastLoadedChunks);

        long chunksLoadedPerSecond = lastLoadedChunks.size() / Config.timeFrameSeconds;

        // Send debug info in the action bar
        if (Config.debugSpeed && player.hasPermission("antigenerationlag.debug")) {
            LocationSpeedUtils.debugSpeed(player, chunksLoadedPerSecond);
        }

        // If we're over the set limit then we want to rubber band the player
        if (chunksLoadedPerSecond > Config.loadLimitPerSecond && !player.hasPermission("antigenerationlag.norubberband")) {
            Vector rubberBandVelocity = player.getVelocity().multiply(-1 * Config.rubberBandModifier);
            Location rubberBandLocation = player.getLocation().add(rubberBandVelocity);
            rubberBandLocation.setY(player.getLocation().getY());
            punishPlayer(player, rubberBandLocation, chunksLoadedPerSecond);
        }
    }

    public static void punishPlayer(Player player, Location rubberBandLocation, long chunksLoadedPerSecond) {
        // Teleport the player back a few blocks before and set its velocity to 0
        player.teleport(rubberBandLocation);
        player.setVelocity(new Vector(0, 0, 0));
        LocationSpeedUtils.resetPlayerTimestamps(player);
        String template = "Rubber banding %s (chunk load %d over %d)";
        String message = String.format(template, player.getName(), Math.round(chunksLoadedPerSecond), Config.loadLimitPerSecond);
        Bukkit.getServer().getLogger().info(message);

        if (Config.warnPlayer) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.warnMessage));
        }

        if (Config.freezePlayer) {
            UUID playerUniqueId = player.getUniqueId();
            long frozenUntil = System.currentTimeMillis() + Config.freezeDurationSeconds * 1000;
            FrozenPlayer frozenPlayer = new FrozenPlayer(player.getLocation(), frozenUntil);
            frozenPlayersLock.lock();
            frozenPlayers.put(playerUniqueId, frozenPlayer);
            frozenPlayersLock.unlock();
        }
    }

    static public void debugSpeed(Player player, long chunksLoadedPerSecond) {
        int percentage = Math.round((chunksLoadedPerSecond * 100L) / Config.loadLimitPerSecond);

        ChatColor messageColor = ChatColor.GREEN;
        if (percentage > 80) {
            messageColor = ChatColor.GOLD;
        }
        if (percentage > 100) {
            messageColor = ChatColor.RED;
        }

        String template = "Chunk load: %d / %d (%d%%)";
        String message = String.format(template, Math.round(chunksLoadedPerSecond), Config.loadLimitPerSecond, percentage);
        BaseComponent[] messageComponent = TextComponent.fromLegacyText(messageColor + message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, messageComponent);
    }

    static public Player getNearestPlayerFromChunk(Chunk chunk) {
        Player nearestPlayer = null;
        double nearestPlayerDistance = Double.MAX_VALUE;

        Location centerChunkLocation = chunk.getBlock(8, 128, 8).getLocation();
        for (Player player : centerChunkLocation.getWorld().getPlayers()) {
            double playerDistanceFromChunk = centerChunkLocation.distance(player.getLocation());

            if (playerDistanceFromChunk < 2048 && playerDistanceFromChunk < nearestPlayerDistance) {
                nearestPlayerDistance = playerDistanceFromChunk;
                nearestPlayer = player;
            }
        }

        return nearestPlayer;
    }

    static public void resetPlayerTimestamps(Player player) {
        UUID playerUniqueId = player.getUniqueId();
        if (chunkTimestamps.containsKey(playerUniqueId)) {
            chunkTimestamps.remove(playerUniqueId);
        }
    }
}