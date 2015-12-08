package com.github.sSuite.sWarp;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import com.github.sSuite.sWarp.exception.WorldMismatchException;

public class Warp {
	private String name;
	private Location location;
	private OfflinePlayer owner;

	public Warp(String name, Location location, OfflinePlayer owner) {
		this.name = name;
		this.location = location;
		this.owner = owner;
	}

	public void teleportPlayer(Player player) throws WorldMismatchException {
		if (!player.getWorld().equals(location.getWorld()) && !player.hasPermission("swarp.go.crossworld")) {
			throw new WorldMismatchException(location.getWorld().getName());
		}

		player.teleport(location, TeleportCause.PLUGIN);
	}

	public void setPlayerCompass(Player player) throws WorldMismatchException {
		if (player.getWorld().equals(location.getWorld())) {
			player.setCompassTarget(location);
		} else {
			throw new WorldMismatchException(location.getWorld().getName());
		}

	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

}
