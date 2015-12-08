package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sWarp.Main;
import com.github.sSuite.sWarp.WarpHandler;
import com.github.sSuite.sWarp.exception.NoSuchWarpException;
import com.github.sSuite.sWarp.exception.WorldMismatchException;

public class GoCommand extends AbstractCommand {

	public GoCommand(Main plugin) {
		super(plugin);
	}

	public GoCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().getLogger().severe("Only players can use /swarp go!");
		}

		if (args.length != 1) {
			return false;
		} else {
			if (!hasPermission(sender)) {
				sender.sendMessage(ChatColor.RED + "You do not have sufficient permissions to do that!");
				return true;
			}

			WarpHandler warpHandler = getPlugin().getWarpHandler();

			try {
				warpHandler.getWarpByName(args[0]).teleportPlayer((Player) sender);
			} catch (WorldMismatchException e) {
				sender.sendMessage(ChatColor.RED + "You must be in world " + ChatColor.RESET + e.getMessage()
						+ ChatColor.RED + " to use this warp!");
				return true;
			} catch (NoSuchWarpException e) {
				sender.sendMessage(
						ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
				return true;
			}

			sender.sendMessage(
					ChatColor.GREEN + "Teleported to warp " + ChatColor.AQUA + args[0] + ChatColor.GREEN + "!");
		}
		return true;
	}

}
