package com.github.sSuite.sWarp;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.scanner.ScannerException;
import com.github.sSuite.sLib.ConfigurationHandler;

public class Main extends JavaPlugin {

	private Configuration configuration;
	public ConfigurationHandler dataHandler;

	@Override
	public void onEnable() {
		configuration = getConfig();
		configuration.options().copyDefaults(true);
		saveConfig();

		dataHandler = new ConfigurationHandler(this, "data");

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
			dataHandler.load();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public ConfigurationHandler getDataHandler() {
		return dataHandler;
	}

}
