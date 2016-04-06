package com.github.ssuite.swarp;

import java.util.Collection;
import java.util.HashMap;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.github.ssuite.swarp.event.RequestAcceptEvent;
import com.github.ssuite.swarp.event.RequestRejectEvent;
import com.github.ssuite.swarp.event.RequestTimedOutEvent;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;

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

		String token = plugin.getRequestService().createToken((Player) source);
		requests.put(token, new Player[] {source, target});

		IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"\", \"extra\": [{\"text\":\"" + source.getName()
				+ "\", \"color\":\"gold\"}, \" wants to track you on their compass. \", {\"text\":\"[Accept]\", \"color\":\"dark_green\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/swarp request accept "
				+ token
				+ "\"}, \"hoverEvent\":{\"action\":\"show_text\", \"value\":\"Click to accept request\"}}, \" \", {\"text\":\"[Reject]\", \"color\":\"dark_red\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/swarp request reject "
				+ token + "\"}, \"hoverEvent\":{\"action\":\"show_text\", \"value\":\"Click to reject request\"}}]}");

		PacketPlayOutChat packet = new PacketPlayOutChat(comp);
		((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);

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
			players[0].sendMessage(ChatColor.GOLD + players[1].getName() + ChatColor.GREEN
					+ " has accepted the tracking request!" + ChatColor.RESET + " Use " + ChatColor.GOLD
					+ "/swarp player" + ChatColor.RESET + " to stop tracking.");
			players[1].sendMessage(ChatColor.GREEN + "You have accepted the tracking request from " + ChatColor.GOLD
					+ players[0].getName() + ChatColor.GREEN + "!");
			registerUpdate(players[0], players[1]);
		}
	}

	@EventHandler
	public void requestReject(RequestRejectEvent event) {
		String token = event.getToken();

		if (requests.containsKey(token)) {
			Player[] players = requests.remove(token);
			players[0].sendMessage(
					ChatColor.GOLD + players[1].getName() + ChatColor.GREEN + " has rejected the tracking request!");
			players[1].sendMessage(ChatColor.GREEN + "You have rejected the tracking request from " + ChatColor.GOLD
					+ players[0].getName() + ChatColor.GREEN + "!");
		}
	}

	@EventHandler
	public void requestTimedOut(RequestTimedOutEvent event) {
		String token = event.getToken();

		if (requests.containsKey(token)) {
			Player[] players = requests.remove(token);
			players[0].sendMessage(
					ChatColor.GOLD + players[1].getName() + ChatColor.GREEN + " failed to respond in time!");
			players[1].sendMessage(ChatColor.RED + "You failed to respond to the tracking request in time!");
		}
	}
}
