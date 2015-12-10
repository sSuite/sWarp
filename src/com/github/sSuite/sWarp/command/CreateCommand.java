package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sWarp.Main;
import com.github.sSuite.sWarp.WarpHandler;
import com.github.sSuite.sWarp.exception.UnsafeWarpNameException;
import com.github.sSuite.sWarp.exception.WarpExistsException;

public class CreateCommand extends AbstractCommand {

	public CreateCommand(Main plugin) {
		super(plugin);
	}

	public CreateCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().getLogger().severe("Only players can use /swarp create!");
		}

		if (args.length != 1 && args.length != 4) {
			return false;
		}

		Location location = ((Player) sender).getLocation();

		if (args.length == 4) {
			try {
				location.setX(Double.parseDouble(args[1]));
				location.setY(Double.parseDouble(args[2]));
				location.setZ(Double.parseDouble(args[3]));
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "The coordinates " + args[1] + ", " + args[2] + ", " + args[3]
						+ " are not valid coordinates!");
				return true;
			}
		}

		WarpHandler warpHandler = getPlugin().getWarpHandler();

		try {
			warpHandler.createWarp(args[0], location, (Player) sender);
		} catch (UnsafeWarpNameException e) {
			sender.sendMessage(ChatColor.RED
					+ "The warp name must only consist of characters from the character set [A-Za-z0-9-_]!");
			return true;
		} catch (WarpExistsException e) {
			sender.sendMessage(ChatColor.RED + "The warp " + ChatColor.RESET + e.getMessage() + ChatColor.RED
					+ " already exists!");
			return true;
		}

		sender.sendMessage(ChatColor.GREEN + "Created warp " + ChatColor.AQUA + args[0] + ChatColor.GREEN + " in world "
				+ ChatColor.GOLD + location.getWorld().getName() + ChatColor.GREEN + "!");

		return true;
	}

}
