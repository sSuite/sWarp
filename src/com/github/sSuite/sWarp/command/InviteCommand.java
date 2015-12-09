package com.github.sSuite.sWarp.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
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

		OfflinePlayer targetPlayer = null;
		OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();

		for (OfflinePlayer offlinePlayer : offlinePlayers) {
			if (offlinePlayer.getName().equalsIgnoreCase(args[1])) {
				targetPlayer = offlinePlayer;
				break;
			}
		}

		targetWarp.invitePlayer(targetPlayer);

		sender.sendMessage(ChatColor.GREEN + "Teleported to warp " + ChatColor.AQUA + targetWarp.getName()
				+ ChatColor.GREEN + "!");

		return false;
	}

}
