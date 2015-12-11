package com.github.sSuite.sWarp;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import com.github.sSuite.sLib.utility.StringUtility;
import com.github.sSuite.sWarp.exception.UnsafeWarpNameException;
import com.github.sSuite.sWarp.exception.WorldMismatchException;

public class Warp {
	private WarpHandler warpHandler;
	private String name;
	private OfflinePlayer owner;
	private boolean isPublic;
	private ArrayList<String> invitedPlayers;
	private Location location;

	public Warp(WarpHandler warpHandler, String name, OfflinePlayer owner, boolean isPublic, Location location) {
		this(warpHandler, name, owner, isPublic, location, new ArrayList<String>());
	}

	public Warp(WarpHandler warpHandler, String name, OfflinePlayer owner, boolean isPublic, Location location,
			ArrayList<String> invitedPlayers) {
		this.warpHandler = warpHandler;
		this.name = name;
		this.owner = owner;
		this.isPublic = isPublic;
		this.invitedPlayers = invitedPlayers;
		this.location = location;
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

	public void setName(String name) throws UnsafeWarpNameException {
		if (!StringUtility.yamlSafe(name)) {
			throw new UnsafeWarpNameException();
		}

		this.name = name;

		warpHandler.save();
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	public void setOwner(OfflinePlayer owner) {
		this.owner = owner;

		warpHandler.save();
	}

	public boolean isOwner(OfflinePlayer player) {
		return owner.equals(player);
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public OfflinePlayer[] getInvitedPlayers() {
		OfflinePlayer[] players = new OfflinePlayer[invitedPlayers.size()];

		for (int i = 0; i < invitedPlayers.size(); i++) {
			players[i] = Bukkit.getServer().getOfflinePlayer(UUID.fromString(invitedPlayers.get(i)));
		}
		return players;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;

		warpHandler.save();
	}

}
