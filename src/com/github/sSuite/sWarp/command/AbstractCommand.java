package com.github.sSuite.sWarp.command;

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

	public abstract boolean execute(CommandSender sender, String[] args);

	public boolean hasPermission(CommandSender sender) {
		if (sender instanceof Player) {
			return sender.isOp() || (permissionNode != null && sender.hasPermission(PERMISSION_ROOT + permissionNode));
		}
		return true;
	}

	public Main getPlugin() {
		return plugin;
	}

}
