package com.github.ssuite.swarp.service;

import com.github.ssuite.slib.ConfigurationHandler;
import com.github.ssuite.slib.utility.StringUtility;
import com.github.ssuite.swarp.Main;
import com.github.ssuite.swarp.Warp;
import com.github.ssuite.swarp.exception.NoSuchWarpException;
import com.github.ssuite.swarp.exception.UnsafeWarpNameException;
import com.github.ssuite.swarp.exception.WarpExistsException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class WarpService {
	
	private Main plugin;
	private Warp[] warps;
	private ConfigurationHandler warpDataHandler;
	private Configuration configuration;
	
	public WarpService(Main plugin) {
		this.plugin = plugin;
		warpDataHandler = plugin.getWarpDataHandler();
		
		load();
	}
	
	public final void load() {
		configuration = warpDataHandler.getConfig();
		ArrayList<String> warpNames = new ArrayList<String>(configuration.getKeys(false));
		
		// Warp sanitization
		for (String warpName : warpNames) {
			if (!configuration.isConfigurationSection(warpName)) {
				configuration.set(warpName, null);
				warpNames.remove(warpName);
			}
		}
		warpDataHandler.save();
		
		warps = new Warp[warpNames.size()];
		
		for (int i = 0; i < warpNames.size(); i++) {
			ConfigurationSection warpSection = configuration.getConfigurationSection(warpNames.get(i));
			
			World world = plugin.getServer().getWorld(warpSection.getString("world"));
			Location location = new Location(world, warpSection.getDouble("x"), warpSection.getDouble("y"), warpSection.getDouble("z"),
					(float) warpSection.getDouble("yaw"), (float) warpSection.getDouble("pitch"));
			
			ArrayList<String> invitedPlayers = new ArrayList<String>();
			invitedPlayers.addAll(warpSection.getStringList("invitees"));
			
			warps[i] = new Warp(this, warpNames.get(i),
					Bukkit.getServer().getOfflinePlayer(UUID.fromString(warpSection.getString("owner"))), warpSection.getBoolean("public"),
					location, invitedPlayers);
		}
		
		save();
	}
	
	public final void save() {
		Set<String> keys = configuration.getKeys(false);
		
		for (String key : keys) {
			configuration.set(key, null);
		}
		
		for (Warp warp : warps) {
			ConfigurationSection warpSection = configuration.createSection(warp.getName());
			
			if (warp.getOwner() != null) {
				warpSection.set("owner", warp.getOwner().getUniqueId().toString());
			}
			
			OfflinePlayer[] invitedPlayers = warp.getInvitedPlayers();
			// if (invitedPlayers.length > 0) {
			String[] invitedUUIDs = new String[invitedPlayers.length];
			
			for (int i = 0; i < invitedPlayers.length; i++) {
				invitedUUIDs[i] = invitedPlayers[i].getUniqueId().toString();
			}
			
			warpSection.set("public", warp.isPublic());
			warpSection.set("invitees", invitedUUIDs);
			warpSection.set("world", warp.getLocation().getWorld().getName());
			warpSection.set("x", warp.getLocation().getX());
			warpSection.set("y", warp.getLocation().getY());
			warpSection.set("z", warp.getLocation().getZ());
			warpSection.set("yaw", warp.getLocation().getYaw());
			warpSection.set("pitch", warp.getLocation().getPitch());
		}
		
		warpDataHandler.save();
	}
	
	public final void createWarp(String name, Player player, boolean isPublic, Location location)
			throws UnsafeWarpNameException, WarpExistsException {
		if (!StringUtility.yamlSafe(name) || name.length() < 0) {
			throw new UnsafeWarpNameException();
		}
		
		try {
			throw new WarpExistsException(getWarpByName(name).getName());
		} catch (NoSuchWarpException e) {
		}
		
		Warp[] temporary = new Warp[warps.length + 1];
		for (int i = 0; i < warps.length; i++) {
			temporary[i] = warps[i];
		}
		
		temporary[warps.length] = new Warp(this, name, player, isPublic, location);
		warps = temporary;
		
		save();
	}
	
	public final void removeWarp(String name) throws NoSuchWarpException {
		Warp targetWarp = getWarpByName(name);
		
		Warp[] temporary = new Warp[warps.length - 1];
		int i = 0;
		for (Warp warp : warps) {
			if (warp != targetWarp) {
				temporary[i] = warps[i];
				i++;
			}
		}
		
		warps = temporary;
		
		configuration.set(targetWarp.getName(), null);
		
		warpDataHandler.save();
	}
	
	public final Warp getWarpByName(String name) throws NoSuchWarpException {
		Warp resultWarp = null;
		
		for (Warp warp : warps) {
			if (warp.getName().equalsIgnoreCase(name)) {
				resultWarp = warp;
				break;
			}
		}
		
		if (resultWarp == null) {
			throw new NoSuchWarpException();
		}
		
		return resultWarp;
	}
	
	public final Warp[] getAllWarps() {
		return warps;
	}
	
	public final Warp[] getAllWarps(Player player) {
		ArrayList<Warp> accessibleWarps = new ArrayList<Warp>();
		
		for (Warp warp : warps) {
			if (warp.isOwner(player) || warp.isPublic() || warp.isInvited(player)) {
				accessibleWarps.add(warp);
			}
		}
		
		return accessibleWarps.toArray(new Warp[0]);
	}
	
	public Main getPlugin() {
		return plugin;
	}
	
}
