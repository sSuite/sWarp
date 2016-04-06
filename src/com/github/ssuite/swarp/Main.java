package com.github.ssuite.swarp;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.yaml.snakeyaml.scanner.ScannerException;
import com.github.ssuite.slib.ConfigurationHandler;

public class Main extends JavaPlugin {

	private final int UPDATE_INTERVAL = 60 * 60 * 24; // 1 day

	private Configuration configuration;
	private BukkitScheduler scheduler;
	public ConfigurationHandler warpDataHandler;

	private WarpHandler warpHandler;
	private PlayerLocationService playerLocationService;
	private RequestService requestService;

	@Override
	public void onEnable() {
		configuration = getConfig();
		configuration.options().copyDefaults(true);
		saveConfig();

		scheduler = Bukkit.getScheduler();
		scheduleUpdateTask();

		warpDataHandler = new ConfigurationHandler(this, "warps");
		warpHandler = new WarpHandler(this);
		playerLocationService = new PlayerLocationService(this);
		requestService = new RequestService(this);

		getServer().getPluginManager().registerEvents(playerLocationService, this);

		getCommand("swarp").setExecutor(new CommandHandler(this));
	}

	@Override
	public void onDisable() {

	}

	public void scheduleUpdateTask() {
		scheduler.cancelTasks(this);

		if (getConfig().getBoolean("notifyOnUpdate")) {
			scheduler.scheduleSyncRepeatingTask(this, new UpdateTask(this), 20L, UPDATE_INTERVAL * 20L);
		}
	}

	public boolean reloadCustomConfig() {
		try {
			reloadConfig();
			configuration = getConfig();
			configuration.options().copyDefaults(true);
			saveConfig();
		} catch (ScannerException e) {
			e.printStackTrace();
			return false;
		}
		try {
			warpDataHandler.load();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		warpHandler.load();

		scheduleUpdateTask();

		return true;
	}

	public ConfigurationHandler getWarpDataHandler() {
		return warpDataHandler;
	}

	public WarpHandler getWarpHandler() {
		return warpHandler;
	}

	public PlayerLocationService getPlayerLocationService() {
		return playerLocationService;
	}

	public RequestService getRequestService() {
		return requestService;
	}

}