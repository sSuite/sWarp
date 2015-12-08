package com.github.sSuite.sWarp;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.scanner.ScannerException;
import com.github.sSuite.sLib.ConfigurationHandler;

public class Main extends JavaPlugin {

	private Configuration configuration;
	public ConfigurationHandler warpDataHandler;

	private WarpHandler warpHandler;

	@Override
	public void onEnable() {
		configuration = getConfig();
		configuration.options().copyDefaults(true);
		saveConfig();

		warpDataHandler = new ConfigurationHandler(this, "warps");
		warpHandler = new WarpHandler(this);

		getCommand("swarp").setExecutor(new CommandHandler(this));
	}

	@Override
	public void onDisable() {

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		warpHandler.loadWarps();

		return true;
	}

	public ConfigurationHandler getWarpDataHandler() {
		return warpDataHandler;
	}

	public WarpHandler getWarpHandler() {
		return warpHandler;
	}

}
