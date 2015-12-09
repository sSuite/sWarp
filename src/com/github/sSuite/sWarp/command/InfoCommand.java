package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.sSuite.sWarp.Main;
import com.github.sSuite.sWarp.Warp;
import com.github.sSuite.sWarp.WarpHandler;
import com.github.sSuite.sWarp.exception.NoSuchWarpException;

public class InfoCommand extends AbstractCommand {

	public InfoCommand(Main plugin) {
		super(plugin);
	}

	public InfoCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			return false;
		}

		WarpHandler warpHandler = getPlugin().getWarpHandler();

		try {
			Warp targetWarp = warpHandler.getWarpByName(args[0]);

			sender.sendMessage(ChatColor.GOLD + "Warp information for " + ChatColor.AQUA + targetWarp.getName()
					+ ChatColor.GOLD + ":");
			sender.sendMessage(ChatColor.YELLOW + "Owner: " + ChatColor.RESET + targetWarp.getOwner().getName());
			sender.sendMessage(ChatColor.YELLOW + "Location:");
			sender.sendMessage(
					ChatColor.YELLOW + "    World: " + ChatColor.RESET + targetWarp.getLocation().getWorld().getName());
			sender.sendMessage(ChatColor.YELLOW + "    X: " + ChatColor.RESET + targetWarp.getLocation().getX());
			sender.sendMessage(ChatColor.YELLOW + "    Y: " + ChatColor.RESET + targetWarp.getLocation().getY());
			sender.sendMessage(ChatColor.YELLOW + "    Z: " + ChatColor.RESET + targetWarp.getLocation().getZ());
			sender.sendMessage(ChatColor.YELLOW + "    Yaw: " + ChatColor.RESET + targetWarp.getLocation().getYaw());
			sender.sendMessage(
					ChatColor.YELLOW + "    Pitch: " + ChatColor.RESET + targetWarp.getLocation().getPitch());
		} catch (NoSuchWarpException e) {
			sender.sendMessage(
					ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
			return true;
		}

		return true;
	}

}
