package com.github.sSuite.sWarp.command;

import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import com.github.sSuite.sLib.utility.NumberUtility;
import com.github.sSuite.sWarp.Main;

public class ListCommand extends AbstractCommand {

	final int ENTRIES_PER_PAGE = 8;

	public ListCommand(Main plugin) {
		super(plugin);
	}

	public ListCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length > 1) {
			return false;
		} else {
			if (!hasPermission(sender)) {
				sender.sendMessage(ChatColor.RED + "You do not have sufficient permissions to do that!");
				return true;
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

			Configuration configuration = getPlugin().getDataHandler().getConfig();
			Set<String> warpsSet = configuration.getKeys(false);
			String[] warps = new String[warpsSet.size()];
			warpsSet.toArray(warps);
			for (String warp : warps) {
				if (!configuration.isConfigurationSection(warp)) {
					configuration.set(warp, null);
				}
			}
			warpsSet = configuration.getKeys(false);
			warps = new String[warpsSet.size()];
			warpsSet.toArray(warps);

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

			sender.sendMessage(
					ChatColor.GREEN + "-----------------sWarp Warp List (" + page + "/" + pages + ")-----------------");
			for (int i = (page - 1) * ENTRIES_PER_PAGE; i < page * ENTRIES_PER_PAGE && i < warps.length; i++) {
				ConfigurationSection warp = configuration.getConfigurationSection(warps[i]);
				sender.sendMessage(ChatColor.AQUA + warp.getName() + ChatColor.RESET + " in " + ChatColor.GOLD
						+ warp.getString("world") + ChatColor.RESET + " @ "
						+ NumberUtility.round(warp.getDouble("x"), 1) + ", "
						+ NumberUtility.round(warp.getDouble("y"), 1) + ", "
						+ NumberUtility.round(warp.getDouble("z"), 1));
			}
			if (page < pages) {
				sender.sendMessage("Use " + ChatColor.YELLOW + "/swarp list " + (page + 1) + ChatColor.RESET
						+ " to see the next page.");
			}
		}
		return true;
	}

}
