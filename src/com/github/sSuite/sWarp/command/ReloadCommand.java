package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sWarp.Main;

public class ReloadCommand extends AbstractCommand {

	public ReloadCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length != 0) {
			return false;
		} else {
			if (!hasPermission(sender)) {
				sender.sendMessage(ChatColor.RED + "You do not have sufficient permissions to do that!");
				return true;
			}

			if (!getPlugin().reloadCustomConfig()) {
				sender.sendMessage(ChatColor.RED
						+ "Plugin configuration failed to reload! Check the console for the stack trace!");
				return true;
			}
			sender.sendMessage(ChatColor.GREEN + "Plugin configuration reloaded!");
		}
		return true;
	}

	private boolean hasPermission(CommandSender sender) {
		if (sender instanceof Player) {
			return sender.isOp();
		}
		return true;
	}

}
