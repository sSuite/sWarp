package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import com.github.sSuite.sLib.utility.StringUtility;
import com.github.sSuite.sWarp.Main;

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

			if (!StringUtility.yamlSafe(args[0])) {
				sender.sendMessage(ChatColor.RED
						+ "The warp name must only consist of characters from the character set [A-Za-z0-9-_]!");
				return true;
			}

			Configuration configuration = getPlugin().getDataHandler().getConfig();
			ConfigurationSection warpSection = configuration.getConfigurationSection(args[0]);
			if (warpSection == null) {
				sender.sendMessage(
						ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
				return true;
			}

			World world = getPlugin().getServer().getWorld(warpSection.getString("world"));
			if (!world.equals(((Player) sender).getLocation().getWorld())) {
				sender.sendMessage(ChatColor.RED + "You must be in world " + ChatColor.RESET + world.getName()
						+ ChatColor.RED + " to use this warp!");
				return true;
			}

			Location location = new Location(world, warpSection.getDouble("x"), warpSection.getDouble("y"),
					warpSection.getDouble("z"));
			((Player) sender).teleport(location);
			sender.sendMessage(
					ChatColor.GREEN + "Teleported to warp " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + "!");
		}
		return true;
	}

}
