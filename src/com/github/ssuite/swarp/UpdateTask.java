package com.github.ssuite.swarp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateTask implements Runnable {
	
	private static final String CURSE_API = "https://api.curseforge.com/servermods/files?projectIds=";
	private static final String PROJECT_ID = "96531";
	private static final String VERSION_REGEX = " ([0-9]+(?:\\.[0-9]+)*)";
	
	private Main plugin;
	
	public UpdateTask(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		try {
			URLConnection connection = new URL(CURSE_API + PROJECT_ID).openConnection();
			PluginDescriptionFile descriptionFile = plugin.getDescription();
			
			connection.addRequestProperty("User-Agent",
					descriptionFile.getName() + "/v" + descriptionFile.getVersion() + " (by " + getAuthors() + ")");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response = reader.readLine();
			reader.close();
			
			JSONArray fileList = (JSONArray) JSONValue.parse(response);
			JSONObject newestFile = null;
			
			for (int i = fileList.size() - 1; i >= 0; i--) {
				JSONObject file = (JSONObject) fileList.get(i);
				if (((String) file.get("releaseType")).equals("release")) {
					newestFile = file;
					break;
				}
			}
			
			if (newestFile == null) {
				plugin.getLogger().warning("No release files could be found! Trying again in one hour.");
				
				plugin.scheduleUpdateTask();
				return;
			}
			
			String latestVersion = (String) newestFile.get("name");
			Pattern pattern = Pattern.compile(plugin.getDescription().getName() + VERSION_REGEX);
			Matcher matcher = pattern.matcher(latestVersion);
			
			if (matcher.find()) {
				latestVersion = matcher.group(1);
				String pluginVersion = plugin.getDescription().getVersion();
				
				if (newer(pluginVersion, latestVersion)) {
					plugin.getLogger().info("An update (current: " + pluginVersion + ", new: " + latestVersion + ") is now available!");
					plugin.getLogger().info("Download it from http://dev.bukkit.org/bukkit-plugins/swarp/");
				} else {
					plugin.getLogger().info("You have the latest version!");
				}
			} else {
				// The latest release file doesn't match the version regex
				plugin.getLogger().warning("Error parsing file details!");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Pattern pattern = Pattern.compile("HTTP response code: (\\d+)");
			Matcher matcher = pattern.matcher(e.getMessage());
			if (!matcher.find()) {
				e.printStackTrace();
			} else {
				plugin.getLogger().warning("Unable to check for updates. Trying again in one hour.");
				
				scheduleUpdateTask();
				return;
			}
		}
	}
	
	private void scheduleUpdateTask() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new UpdateTask(plugin), 60 * 60 * 20L);
	}
	
	private String getAuthors() {
		StringBuilder stringBuilder = new StringBuilder();
		String[] authors = new String[0];
		authors = plugin.getDescription().getAuthors().toArray(authors);
		for (int i = 0; i < authors.length; i++) {
			stringBuilder.append(authors[i] + (i + 1 < authors.length ? ", " : ""));
		}
		return stringBuilder.toString();
	}
	
	private final boolean newer(String currentVersion, String checkVersion) {
		String[] currentParts = currentVersion.split("\\.");
		String[] checkParts = checkVersion.split("\\.");
		
		for (int i = 0; i < Math.min(currentParts.length, checkParts.length); i++) {
			int current = Integer.parseInt(currentParts[i]);
			int check = Integer.parseInt(checkParts[i]);
			if (current < check) {
				return true;
			}
		}
		if (currentParts.length < checkParts.length) {
			return true;
		}
		
		return false;
	}
	
}
