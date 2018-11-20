package com.github.ssuite.swarp.command;

import com.github.ssuite.swarp.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends AbstractCommand {
	
	public ReloadCommand(Main plugin) {
		super(plugin);
	}
	
	public ReloadCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}
	
	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (args.length != 0) {
			return false;
		}
		
		if (!getPlugin().reloadCustomConfig()) {
			sender.sendMessage(ChatColor.RED + "Plugin configuration failed to reload! Check the console for the stack trace!");
			return true;
		}
		sender.sendMessage(ChatColor.GREEN + "Plugin configuration reloaded!");
		
		return true;
	}
	
}
