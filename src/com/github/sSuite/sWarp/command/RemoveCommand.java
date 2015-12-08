package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.sSuite.sWarp.Main;
import com.github.sSuite.sWarp.WarpHandler;
import com.github.sSuite.sWarp.exception.NoSuchWarpException;

public class RemoveCommand extends AbstractCommand {

	public RemoveCommand(Main plugin) {
		super(plugin);
	}

	public RemoveCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			return false;
		} else {
			if (!hasPermission(sender)) {
				sender.sendMessage(ChatColor.RED + "You do not have sufficient permissions to do that!");
				return true;
			}

			WarpHandler warpHandler = getPlugin().getWarpHandler();

			try {
				warpHandler.removeWarp(args[0]);
			} catch (NoSuchWarpException e) {
				sender.sendMessage(
						ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
				return true;
			}

			sender.sendMessage(
					ChatColor.GREEN + "Removed warp \"" + ChatColor.AQUA + args[0] + ChatColor.GREEN + "\"!");
		}
		return true;
	}

}
