package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import com.github.sSuite.sLib.utility.StringUtility;
import com.github.sSuite.sWarp.Main;

public class RemoveCommand extends AbstractCommand {

	public RemoveCommand(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length != 1 && args.length != 4) {
			return false;
		} else {
			if (!StringUtility.yamlSafe(args[0])) {
				sender.sendMessage(ChatColor.RED + "The warp name must only consist of alphanumeric characters!");
				return true;
			}

			Configuration configuration = getPlugin().getDataHandler().getConfig();
			ConfigurationSection warpSection = configuration.getConfigurationSection(args[0]);
			if (warpSection == null) {
				sender.sendMessage(
						ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
				return true;
			} else {
				configuration.set(args[0], null);
			}

			getPlugin().getDataHandler().save();

			sender.sendMessage(ChatColor.AQUA + "Removed warp \"" + ChatColor.WHITE + args[0] + ChatColor.AQUA + "\"!");
		}
		return true;
	}

}
