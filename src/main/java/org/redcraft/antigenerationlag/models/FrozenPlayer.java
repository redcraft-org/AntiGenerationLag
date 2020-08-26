package org.redcraft.antigenerationlag.models;

import org.bukkit.Location;

public class FrozenPlayer {
    public Location location;
    public long frozenUntil;

    public FrozenPlayer(Location location, long frozenUntil) {
        this.location = location;
        this.frozenUntil = frozenUntil;
    }
}