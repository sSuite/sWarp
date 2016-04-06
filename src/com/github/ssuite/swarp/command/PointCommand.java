package com.github.ssuite.swarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.ssuite.swarp.Main;
import com.github.ssuite.swarp.Warp;
import com.github.ssuite.swarp.WarpHandler;
import com.github.ssuite.swarp.exception.NoSuchWarpException;
import com.github.ssuite.swarp.exception.WorldMismatchException;

public class PointCommand extends AbstractCommand {

	public PointCommand(Main plugin) {
		super(plugin);
	}

	public PointCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().getLogger().severe("Only players can use /swarp point!");
			return true;
		}

		if (args.length != 0 && args.length != 1) {
			return false;
		}

		if (args.length == 0) {
			getPlugin().getPlayerLocationService().cancelUpdate((Player) sender);
			((Player) sender).setCompassTarget(((Player) sender).getWorld().getSpawnLocation());

			sender.sendMessage(ChatColor.GREEN + "Restored compass location!");
		} else {

			WarpHandler warpHandler = getPlugin().getWarpHandler();
			Warp targetWarp = null;

			try {
				targetWarp = warpHandler.getWarpByName(args[0]);

				if (!targetWarp.isPublic() && !targetWarp.isOwner((Player) sender)
						&& !targetWarp.isInvited((Player) sender) && !sender.hasPermission("swarp.point.all")) {
					sender.sendMessage(ChatColor.RED + "You do not own that warp nor were you invited to it!");
					return true;
				}

				targetWarp.setPlayerCompass((Player) sender);
			} catch (WorldMismatchException e) {
				sender.sendMessage(ChatColor.RED + "You must be in world " + ChatColor.RESET + e.getMessage()
						+ ChatColor.RED + " to use this warp!");
				return true;
			} catch (NoSuchWarpException e) {
				sender.sendMessage(
						ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
				return true;
			}

			sender.sendMessage(ChatColor.GREEN + "Set compass to point to warp " + ChatColor.AQUA + targetWarp.getName()
					+ ChatColor.GREEN + "!");
		}

		return true;
	}

}