package com.github.ssuite.swarp.command;

import com.github.ssuite.swarp.Main;
import com.github.ssuite.swarp.exception.UnsafeWarpNameException;
import com.github.ssuite.swarp.exception.WarpExistsException;
import com.github.ssuite.swarp.service.WarpService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CreateCommand extends AbstractCommand {
	
	public CreateCommand(Main plugin) {
		super(plugin);
	}
	
	public CreateCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}
	
	@Override
	public ArrayList<String> processFlags() {
		ArrayList<String> flags = getFlags();
		
		for (String flag : flags) {
			if (flag.equals("private")) {
				if (!flags.contains("p")) {
					flags.add("p");
				}
				
				flags.remove("private");
			}
		}
		
		return flags;
	}
	
	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().getLogger().severe("Only players can use /swarp create!");
			return true;
		}
		
		if (args.length != 1 && args.length != 4) {
			return false;
		}
		
		boolean isPublic = !getFlags().contains("p");
		
		Location location = ((Player) sender).getLocation();
		
		if (args.length == 4) {
			try {
				location.setX(Double.parseDouble(args[1]));
				location.setY(Double.parseDouble(args[2]));
				location.setZ(Double.parseDouble(args[3]));
			} catch (NumberFormatException e) {
				sender.sendMessage(
						ChatColor.RED + "The coordinates " + args[1] + ", " + args[2] + ", " + args[3] + " are not valid coordinates!");
				return true;
			}
		}
		
		WarpService warpService = getPlugin().getWarpService();
		
		try {
			warpService.createWarp(args[0], (Player) sender, isPublic, location);
		} catch (UnsafeWarpNameException e) {
			sender.sendMessage(ChatColor.RED + "The warp name must only consist of characters from the character set [A-Za-z0-9-_]!");
			return true;
		} catch (WarpExistsException e) {
			sender.sendMessage(ChatColor.RED + "The warp " + ChatColor.RESET + e.getMessage() + ChatColor.RED + " already exists!");
			return true;
		}
		
		sender.sendMessage(
				ChatColor.GREEN + "Created " + (isPublic ? "public" : "private") + " warp " + ChatColor.AQUA + args[0] + ChatColor.GREEN +
						" in world " + ChatColor.GOLD + location.getWorld().getName() + ChatColor.GREEN + "!");
		
		return true;
	}
	
}
