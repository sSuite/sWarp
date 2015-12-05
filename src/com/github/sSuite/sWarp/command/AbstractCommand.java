package com.github.sSuite.sWarp.command;

import org.bukkit.command.CommandSender;
import com.github.sSuite.sWarp.Main;

public abstract class AbstractCommand {

	private Main plugin;

	public AbstractCommand(Main plugin) {
		this.plugin = plugin;
	}

	public abstract boolean execute(CommandSender sender, String[] args);

	public Main getPlugin() {
		return plugin;
	}

}
