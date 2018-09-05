package com.github.ssuite.swarp.service;

import com.github.ssuite.slib.event.RequestAcceptEvent;
import com.github.ssuite.slib.event.RequestRejectEvent;
import com.github.ssuite.slib.event.RequestTimedOutEvent;
import com.github.ssuite.slib.utility.ChatUtility;
import com.github.ssuite.swarp.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.HashMap;

public class PlayerLocationService implements Listener {
	private Main plugin;
	private HashMap<Player, BukkitTask> tasks;
	private HashMap<String, Player[]> requests;
	
	public PlayerLocationService(Main plugin) {
		this.plugin = plugin;
		tasks = new HashMap<Player, BukkitTask>();
		requests = new HashMap<String, Player[]>();
	}
	
	public boolean requestTrack(Player source, Player target) {
		Collection<Player[]> playerRequests = requests.values();
		for (Player[] players : playerRequests) {
			if (players[0].equals(source) && players[1].equals(target)) {
				return false;
			}
		}
		
		String token = plugin.getRequestService().createToken(source, 30);
		requests.put(token, new Player[]{source, target});
		
		ChatUtility.sendJSONMessage(target, "{\"text\":\"\", \"extra\": [{\"text\":\"" + source.getName() +
				"\", \"color\":\"gold\"}, \" wants to track you on their compass.\\nClick to \", {\"text\":\"[Accept]\", \"color\":\"dark_green\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/swarp request accept " +
				token +
				"\"}, \"hoverEvent\":{\"action\":\"show_text\", \"value\":\"Click to accept request\"}}, \" or \", {\"text\":\"[Reject]\", \"color\":\"dark_red\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/swarp request reject " +
				token + "\"}, \"hoverEvent\":{\"action\":\"show_text\", \"value\":\"Click to reject request\"}}]}");
		
		return true;
	}
	
	public void registerUpdate(Player source, Player target) {
		if (tasks.containsKey(source)) {
			tasks.get(source).cancel();
		}
		
		tasks.put(source, new BukkitRunnable() {
			@Override
			public void run() {
				source.setCompassTarget(target.getLocation());
			}
		}.runTaskTimer(plugin, 0, 10L));
	}
	
	public void cancelUpdate(Player source) {
		if (tasks.containsKey(source)) {
			tasks.get(source).cancel();
			source.setCompassTarget(source.getWorld().getSpawnLocation());
			tasks.remove(source);
		}
	}
	
	@EventHandler
	public void requestAccept(RequestAcceptEvent event) {
		String token = event.getToken();
		
		if (requests.containsKey(token)) {
			Player[] players = requests.remove(token);
			players[0].sendMessage(
					ChatColor.GOLD + players[1].getName() + ChatColor.RESET + " has " + ChatColor.GREEN + "accepted" +
							ChatColor.RESET + " your tracking request! Your compass is now tracking their location." +
							ChatColor.RESET + "\nUse " + ChatColor.GOLD + "/swarp player" + ChatColor.RESET +
							" to stop tracking.");
			players[1].sendMessage(
					"You have " + ChatColor.GREEN + "accepted" + ChatColor.RESET + " the tracking request from " +
							ChatColor.GOLD + players[0].getName() + ChatColor.RESET + "!");
			registerUpdate(players[0], players[1]);
		}
	}
	
	@EventHandler
	public void requestReject(RequestRejectEvent event) {
		String token = event.getToken();
		
		if (requests.containsKey(token)) {
			Player[] players = requests.remove(token);
			players[0].sendMessage(
					ChatColor.GOLD + players[1].getName() + ChatColor.RESET + " has " + ChatColor.RED + "rejected" +
							ChatColor.RESET + " your tracking request!");
			players[1].sendMessage(
					"You have " + ChatColor.RED + "rejected" + ChatColor.RESET + " the tracking request from " +
							ChatColor.GOLD + players[0].getName() + ChatColor.RESET + "!");
		}
	}
	
	@EventHandler
	public void requestTimedOut(RequestTimedOutEvent event) {
		String token = event.getToken();
		
		if (requests.containsKey(token)) {
			Player[] players = requests.remove(token);
			players[0].sendMessage(ChatColor.GOLD + players[1].getName() + ChatColor.RED +
					" did not respond to your tracking request in time!");
			players[1].sendMessage(
					ChatColor.RED + "You did not respond to the tracking request from " + ChatColor.GOLD +
							players[0].getName() + ChatColor.RED + " in time!");
		}
	}
}
