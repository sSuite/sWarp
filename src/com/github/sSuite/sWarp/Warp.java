package com.github.sSuite.sWarp;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import com.github.sSuite.sWarp.exception.WorldMismatchException;

public class Warp {
	private WarpHandler warpHandler;
	private String name;
	private Location location;
	private OfflinePlayer owner;
	private ArrayList<String> invitedPlayers;

	public Warp(WarpHandler warpHandler, String name, Location location, OfflinePlayer owner) {
		this(warpHandler, name, location, owner, new ArrayList<String>());
	}

	public Warp(WarpHandler warpHandler, String name, Location location, OfflinePlayer owner,
			ArrayList<String> invitedPlayers) {
		this.warpHandler = warpHandler;
		this.name = name;
		this.location = location;
		this.owner = owner;
		this.invitedPlayers = invitedPlayers;
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

	public boolean isOwner(OfflinePlayer player) {
		return owner.equals(player);
	}

	public boolean invitePlayer(OfflinePlayer player) {
		if (isInvited(player)) {
			return false;
		}

		invitedPlayers.add(player.getUniqueId().toString());

		warpHandler.save();
		return true;
	}

	public boolean uninvitePlayer(OfflinePlayer player) {
		return invitedPlayers.remove(player.getUniqueId().toString());
	}

	public boolean isInvited(OfflinePlayer player) {
		return invitedPlayers.contains(player.getUniqueId().toString());
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

	public OfflinePlayer[] getInvitedPlayers() {
		OfflinePlayer[] players = new OfflinePlayer[invitedPlayers.size()];

		for (int i = 0; i < invitedPlayers.size(); i++) {
			players[i] = Bukkit.getServer().getOfflinePlayer(UUID.fromString(invitedPlayers.get(i)));
		}
		return players;
	}

}
