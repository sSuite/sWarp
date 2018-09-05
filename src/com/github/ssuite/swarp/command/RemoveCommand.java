package com.github.ssuite.swarp.command;

import com.github.ssuite.swarp.Main;
import com.github.ssuite.swarp.Warp;
import com.github.ssuite.swarp.exception.NoSuchWarpException;
import com.github.ssuite.swarp.service.WarpService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand extends AbstractCommand {
	
	public RemoveCommand(Main plugin) {
		super(plugin);
	}
	
	public RemoveCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}
	
	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (args.length != 1) {
			return false;
		}
		
		WarpService warpService = getPlugin().getWarpService();
		Warp targetWarp = null;
		
		try {
			targetWarp = warpService.getWarpByName(args[0]);
			
			if (!targetWarp.isOwner((Player) sender) && !sender.hasPermission("swarp.remove.all")) {
				sender.sendMessage(ChatColor.RED + "You do not own that warp!");
				return true;
			}
			
			warpService.removeWarp(args[0]);
		} catch (NoSuchWarpException e) {
			sender.sendMessage(
					ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
			return true;
		}
		
		sender.sendMessage(
				ChatColor.GREEN + "Removed warp " + ChatColor.AQUA + targetWarp.getName() + ChatColor.GREEN + "!");
		
		return true;
	}
	
}
