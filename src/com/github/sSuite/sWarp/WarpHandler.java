package com.github.sSuite.sWarp;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import com.github.sSuite.sLib.ConfigurationHandler;
import com.github.sSuite.sLib.utility.StringUtility;
import com.github.sSuite.sWarp.exception.NoSuchWarpException;
import com.github.sSuite.sWarp.exception.UnsafeWarpNameException;
import com.github.sSuite.sWarp.exception.WarpExistsException;

public class WarpHandler {

	private Main plugin;
	private Warp[] warps;
	private ConfigurationHandler warpDataHandler;
	private Configuration configuration;

	public WarpHandler(Main plugin) {
		this.plugin = plugin;
		this.warpDataHandler = plugin.getWarpDataHandler();

		loadWarps();
	}

	public final void loadWarps() {
		configuration = warpDataHandler.getConfig();
		ArrayList<String> warpNames = new ArrayList<String>(configuration.getKeys(false));

		// Warp sanitization
		for (String warpName : warpNames) {
			if (!configuration.isConfigurationSection(warpName)) {
				configuration.set(warpName, null);
				warpNames.remove(warpName);
			}
		}
		plugin.getWarpDataHandler().save();

		warps = new Warp[warpNames.size()];

		for (int i = 0; i < warpNames.size(); i++) {
			ConfigurationSection warpSection = configuration.getConfigurationSection(warpNames.get(i));

			World world = plugin.getServer().getWorld(warpSection.getString("world"));
			Location location = new Location(world, warpSection.getDouble("x"), warpSection.getDouble("y"),
					warpSection.getDouble("z"), (float) warpSection.getDouble("yaw"),
					(float) warpSection.getDouble("pitch"));

			String uuidString = warpSection.getString("owner");

			if (uuidString != null) {
				warps[i] = new Warp(warpNames.get(i), location,
						Bukkit.getServer().getOfflinePlayer(UUID.fromString(uuidString)));
			} else {
				warps[i] = new Warp(warpNames.get(i), location, null);
			}
		}
	}

	public final void createWarp(String name, Location location) throws UnsafeWarpNameException, WarpExistsException {
		createWarp(name, location, null);
	}

	public final void createWarp(String name, Location location, Player player)
			throws UnsafeWarpNameException, WarpExistsException {
		if (!StringUtility.yamlSafe(name)) {
			throw new UnsafeWarpNameException();
		}

		ConfigurationSection warpSection = configuration.getConfigurationSection(name);
		if (warpSection == null) {
			warpSection = configuration.createSection(name);
		} else {
			// throw new WarpException(WarpExceptionReason.WARP_EXISTS);
			throw new WarpExistsException();
		}

		if (player != null) {
			warpSection.set("owner", player.getUniqueId().toString());
		}
		warpSection.set("world", location.getWorld().getName());
		warpSection.set("x", location.getX());
		warpSection.set("y", location.getY());
		warpSection.set("z", location.getZ());
		warpSection.set("yaw", location.getYaw());
		warpSection.set("pitch", location.getPitch());

		warpDataHandler.save();

		Warp[] temporary = new Warp[warps.length + 1];
		for (int i = 0; i < warps.length; i++) {
			temporary[i] = warps[i];
		}

		temporary[warps.length] = new Warp(name, location, player);
	}

	public final void removeWarp(String name) throws NoSuchWarpException {
		ConfigurationSection warpSection = configuration.getConfigurationSection(name);
		if (warpSection == null) {
			// throw new WarpException(WarpExceptionReason.NO_SUCH_WARP);
			throw new NoSuchWarpException();
		} else {
			configuration.set(name, null);
		}

		warpDataHandler.save();
	}

	public final Warp getWarpByName(String name) throws NoSuchWarpException {
		Warp resultWarp = null;

		for (Warp warp : warps) {
			if (warp.getName().equals(name)) {
				resultWarp = warp;
				break;
			}
		}

		if (resultWarp == null) {
			// throw new WarpException(WarpExceptionReason.NO_SUCH_WARP);
			throw new NoSuchWarpException();
		}

		return resultWarp;
	}

	public final Warp[] getAllWarps() {
		return warps;
	}

}
