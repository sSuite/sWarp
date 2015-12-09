package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sWarp.Main;
import com.github.sSuite.sWarp.Warp;
import com.github.sSuite.sWarp.WarpHandler;
import com.github.sSuite.sWarp.exception.NoSuchWarpException;

public class UninviteCommand extends AbstractCommand {

	public UninviteCommand(Main plugin) {
		super(plugin);
	}

	public UninviteCommand(Main plugin, String permissionNode) {
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
		OfflinePlayer[] invitedPlayers = targetWarp.getInvitedPlayers();

		for (OfflinePlayer offlinePlayer : invitedPlayers) {
			if (offlinePlayer.getName().equalsIgnoreCase(args[1])) {
				targetPlayer = offlinePlayer;
				break;
			}
		}

		if (targetPlayer != null && targetWarp.uninvitePlayer(targetPlayer)) {
			sender.sendMessage(ChatColor.GREEN + "Uninvited " + ChatColor.GOLD + targetPlayer.getName()
					+ ChatColor.RESET + " from " + ChatColor.AQUA + targetWarp.getName() + ChatColor.GREEN + "!");
		} else {
			sender.sendMessage(ChatColor.GOLD + targetPlayer.getName() + ChatColor.RED + " has not been invited to "
					+ ChatColor.AQUA + targetWarp.getName() + ChatColor.RESET + "!");
		}

		return true;
	}

}
