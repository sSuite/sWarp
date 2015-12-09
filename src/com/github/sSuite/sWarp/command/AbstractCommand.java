package com.github.sSuite.sWarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sWarp.Main;

public abstract class AbstractCommand {

	private static final String PERMISSION_ROOT = "swarp.";

	private Main plugin;
	private String permissionNode;

	public AbstractCommand(Main plugin) {
		this.plugin = plugin;
	}

	public AbstractCommand(Main plugin, String permissionNode) {
		this.plugin = plugin;
		this.permissionNode = permissionNode;
	}

	/**
	 * Executes before the command logic. Calls and returns the value of
	 * <code>onExecute</code>.
	 * 
	 * @param sender
	 *            - the command sender
	 * @param args
	 *            - the command arguments
	 * @return whether or not the sWarp command help should be shown
	 */
	public boolean execute(CommandSender sender, String[] args) {
		if (!hasPermission(sender)) {
			sender.sendMessage(ChatColor.RED + "You do not have sufficient permissions to do that!");
			return true;
		}

		return onExecute(sender, args);
	}

	public abstract boolean onExecute(CommandSender sender, String[] args);

	/**
	 * Checks whether or not the <code>sender</code> has permission to run the
	 * command.
	 * 
	 * @param sender
	 *            - the command sender
	 * @return whether or not the sender has permission
	 */
	public boolean hasPermission(CommandSender sender) {
		if (sender instanceof Player) {
			return permissionNode != null && sender.hasPermission(PERMISSION_ROOT + permissionNode);
		}
		return true;
	}

	/**
	 * @return the <code>Main</code> instance of the plugin
	 */
	public Main getPlugin() {
		return plugin;
	}

}
