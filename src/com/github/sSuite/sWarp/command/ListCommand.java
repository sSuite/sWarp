package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sLib.utility.CommandHelpUtility;
import com.github.sSuite.sLib.utility.MonospaceUtility;
import com.github.sSuite.sLib.utility.NumberUtility;
import com.github.sSuite.sWarp.Main;
import com.github.sSuite.sWarp.Warp;
import com.github.sSuite.sWarp.WarpHandler;

public class ListCommand extends AbstractCommand {

	final int ENTRIES_PER_PAGE = 8;

	public ListCommand(Main plugin) {
		super(plugin);
	}

	public ListCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (args.length > 1) {
			return false;
		}

		int page = 1;
		if (args.length == 1) {
			try {
				page = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + args[0] + " is not a valid number!");
				return true;
			}
		}

		WarpHandler warpHandler = getPlugin().getWarpHandler();
		Warp[] warps;

		if (sender instanceof Player) {
			warps = warpHandler.getAllWarps((Player) sender);
		} else {
			warps = warpHandler.getAllWarps();
		}

		if (warps.length == 0) {
			sender.sendMessage(ChatColor.RED + "There are no warps to list!");
			return true;
		}

		int pages = warps.length / ENTRIES_PER_PAGE + (warps.length % ENTRIES_PER_PAGE == 0 ? 0 : 1);

		if (page > pages) {
			page = pages;
		} else if (page < 1) {
			page = 1;
		}

		sender.sendMessage(CommandHelpUtility.createHeader("sWarp Warp List (" + ChatColor.GOLD + page + ChatColor.RESET
				+ "/" + ChatColor.GOLD + pages + ChatColor.RESET + ")", sender));
		for (int i = (page - 1) * ENTRIES_PER_PAGE; i < page * ENTRIES_PER_PAGE && i < warps.length; i++) {
			Warp warp = warps[i];
			String info = ChatColor.AQUA + warp.getName() + ChatColor.RESET + " by " + ChatColor.GOLD
					+ warp.getOwner().getName() + ChatColor.RESET;
			String location = "@ " + NumberUtility.roundString(warp.getLocation().getX()) + ", "
					+ NumberUtility.roundString(warp.getLocation().getY()) + ", "
					+ NumberUtility.roundString(warp.getLocation().getZ()) + " in " + ChatColor.GOLD
					+ warp.getLocation().getWorld().getName();

			sender.sendMessage(MonospaceUtility.fillToWidthWithString(info, " ",
					(sender instanceof Player ? MonospaceUtility.DEFAULT_CLIENT_WIDTH
							: MonospaceUtility.DEFAULT_MONOSPACE_WIDTH)
							- MonospaceUtility.getStringWidth(location, !(sender instanceof Player)),
					!(sender instanceof Player)) + location);
		}
		if (page < pages) {
			sender.sendMessage("Use " + ChatColor.YELLOW + "/swarp list " + (page + 1) + ChatColor.RESET
					+ " to see the next page.");
		}

		return true;
	}

}
