package com.github.sSuite.sWarp.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sWarp.Main;
import com.github.sSuite.sWarp.Warp;
import com.github.sSuite.sWarp.WarpHandler;
import com.github.sSuite.sWarp.exception.NoSuchWarpException;

public class InviteCommand extends AbstractCommand {

	public InviteCommand(Main plugin) {
		super(plugin);
	}

	public InviteCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (args.length != 2) {
			return false;
		}

		WarpHandler warpHandler = getPlugin().getWarpHandler();
		Warp targetWarp = null;

		try {
			targetWarp = warpHandler.getWarpByName(args[0]);
		} catch (NoSuchWarpException e) {
			sender.sendMessage(
					ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
			return true;
		}

		if (!targetWarp.isOwner((Player) sender) && !sender.hasPermission("swarp.invite.all")) {
			sender.sendMessage(ChatColor.RED + "You do not own that warp!");
			return true;
		}

		OfflinePlayer targetPlayer = null;
		OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();

		for (OfflinePlayer offlinePlayer : offlinePlayers) {
			if (offlinePlayer.getName().equalsIgnoreCase(args[1])) {
				targetPlayer = offlinePlayer;
				break;
			}
		}

		if (targetPlayer == null) {
			sender.sendMessage(
					ChatColor.RED + "That player has never logged in to the server and so could not be found!");
			return true;
		}

		if (targetWarp.invitePlayer(targetPlayer)) {
			sender.sendMessage(ChatColor.GREEN + "Invited " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.RESET
					+ " to " + ChatColor.AQUA + targetWarp.getName() + ChatColor.GREEN + "!");
		} else {
			sender.sendMessage(ChatColor.GOLD + targetPlayer.getName() + ChatColor.RED + " has already been invited to "
					+ ChatColor.AQUA + targetWarp.getName() + ChatColor.RED + "!");
		}

		return true;
	}

}
