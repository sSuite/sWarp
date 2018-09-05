package com.github.ssuite.swarp.service;

import com.github.ssuite.swarp.event.RequestAcceptEvent;
import com.github.ssuite.swarp.event.RequestRejectEvent;
import com.github.ssuite.swarp.event.RequestTimedOutEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RequestService {
	private static final String RANDOM_SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int TOKEN_LENGTH = 10;
	private static final int DEFAULT_DURATION = 60;
	
	private Plugin plugin;
	private Random random;
	private ArrayList<char[]> tokens;
	private ArrayList<Player> players;
	private ArrayList<BukkitTask> tasks;
	
	public RequestService(Plugin plugin) {
		this.plugin = plugin;
		random = new Random();
		tokens = new ArrayList<char[]>();
		players = new ArrayList<Player>();
		tasks = new ArrayList<BukkitTask>();
	}
	
	public String createToken(Player player) {
		return createToken(player, DEFAULT_DURATION);
	}
	
	public String createToken(Player player, int duration) {
		char[] token = new char[TOKEN_LENGTH];
		
		while (true) {
			for (int i = 0; i < token.length; i++) {
				token[i] = RANDOM_SOURCE.charAt(random.nextInt(RANDOM_SOURCE.length()));
			}
			
			for (int i = 0; i < tokens.size(); i++) {
				if (Arrays.equals(token, tokens.get(i))) {
					continue;
				}
			}
			
			break;
		}
		
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				String stringToken = new String(token);
				plugin.getServer().getPluginManager().callEvent(
						new RequestTimedOutEvent(getPlayer(stringToken), stringToken));
				removeToken(stringToken);
			}
		}.runTaskLater(plugin, duration * 20L);
		tokens.add(token);
		players.add(player);
		tasks.add(task);
		
		return new String(token);
	}
	
	public boolean hasToken(String token) {
		for (int i = 0; i < tokens.size(); i++) {
			if (new String(tokens.get(i)).equals(token)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeToken(String token) {
		for (int i = 0; i < tokens.size(); i++) {
			if (new String(tokens.get(i)).equals(token)) {
				tokens.remove(i);
				players.remove(i);
				tasks.remove(i).cancel();
				return true;
			}
		}
		
		return false;
	}
	
	public Player getPlayer(String token) {
		for (int i = 0; i < tokens.size(); i++) {
			if (new String(tokens.get(i)).equals(token)) {
				return players.get(i);
			}
		}
		
		return null;
	}
	
	public void acceptRequest(String token) {
		if (hasToken(token)) {
			plugin.getServer().getPluginManager().callEvent(new RequestAcceptEvent(getPlayer(token), token));
			removeToken(token);
		}
	}
	
	public void rejectRequest(String token) {
		if (hasToken(token)) {
			plugin.getServer().getPluginManager().callEvent(new RequestRejectEvent(getPlayer(token), token));
			removeToken(token);
		}
	}
}
