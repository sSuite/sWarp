package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sWarp.Main;

public class StopCommand extends AbstractCommand {

	public StopCommand(Main plugin) {
		super(plugin);
	}

	public StopCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().getLogger().severe("Only players can use /swarp stop!");
		}

		if (args.length != 0) {
			return false;
		} else {
			if (!hasPermission(sender)) {
				sender.sendMessage(ChatColor.RED + "You do not have sufficient permissions to do that!");
				return true;
			}

			((Player) sender).setCompassTarget(((Player) sender).getWorld().getSpawnLocation());
			sender.sendMessage(ChatColor.GREEN + "Restored compass location!");
		}
		return true;
	}

}
