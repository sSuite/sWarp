package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import com.github.sSuite.sLib.utility.StringUtility;
import com.github.sSuite.sWarp.Main;

public class CreateCommand extends AbstractCommand {

	public CreateCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().getLogger().severe("Only players can use /swarp create!");
		}

		if (args.length != 1 && args.length != 4) {
			return false;
		} else {
			if (!StringUtility.yamlSafe(args[0])) {
				sender.sendMessage(ChatColor.RED
						+ "The warp name must only consist of characters from the character set [A-Za-z0-9-_]!");
				return true;
			}

			Location location = ((Player) sender).getLocation();
			String world = location.getWorld().getName();
			double x, y, z;

			if (args.length == 1) {
				x = location.getX();
				y = location.getY();
				z = location.getZ();
			} else {
				try {
					x = Double.parseDouble(args[1]);
					y = Double.parseDouble(args[2]);
					z = Double.parseDouble(args[3]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "The coordinates " + args[1] + ", " + args[2] + ", " + args[3]
							+ " are not valid coordinates!");
					return true;
				}
			}

			Configuration configuration = getPlugin().getDataHandler().getConfig();
			ConfigurationSection warpSection = configuration.getConfigurationSection(args[0]);
			if (warpSection == null) {
				warpSection = configuration.createSection(args[0]);
			} else {
				sender.sendMessage(
						ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " already exists!");
				return true;
			}

			warpSection.set("world", world);
			warpSection.set("x", x);
			warpSection.set("y", y);
			warpSection.set("z", z);

			getPlugin().getDataHandler().save();

			sender.sendMessage(ChatColor.AQUA + "Created warp \"" + ChatColor.WHITE + args[0] + ChatColor.AQUA
					+ "\" in world " + ChatColor.WHITE + world + ChatColor.AQUA + "!");
		}
		return true;
	}

}
