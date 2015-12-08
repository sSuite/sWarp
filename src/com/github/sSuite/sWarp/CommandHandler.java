package com.github.sSuite.sWarp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sLib.utility.CommandHelpUtility;
import com.github.sSuite.sWarp.command.AbstractCommand;
import com.github.sSuite.sWarp.command.CreateCommand;
import com.github.sSuite.sWarp.command.GoCommand;
import com.github.sSuite.sWarp.command.ListCommand;
import com.github.sSuite.sWarp.command.PointCommand;
import com.github.sSuite.sWarp.command.ReloadCommand;
import com.github.sSuite.sWarp.command.RemoveCommand;
import com.github.sSuite.sWarp.command.StopCommand;

public class CommandHandler implements CommandExecutor {

	private Main plugin;

	public CommandHandler(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			showHelp(sender);
			return true;
		}

		String[] newArgs = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			newArgs[i - 1] = args[i];
		}

		AbstractCommand commandClass;
		switch (args[0].toLowerCase()) {
			case "reload":
				commandClass = new ReloadCommand(plugin, "reload");
				break;
			case "list":
				commandClass = new ListCommand(plugin, "list");
				break;
			case "create":
				commandClass = new CreateCommand(plugin, "create");
				break;
			case "remove":
				commandClass = new RemoveCommand(plugin, "remove");
				break;
			case "go":
				commandClass = new GoCommand(plugin, "go");
				break;
			case "point":
				commandClass = new PointCommand(plugin, "point");
				break;
			case "stop":
				commandClass = new StopCommand(plugin, "point");
				break;
			default:
				showHelp(sender);
				return true;
		}

		if (!commandClass.execute(sender, newArgs)) {
			showHelp(sender);
		}
		return true;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(CommandHelpUtility.createHeader("sWarp", 22));
		sender.sendMessage(CommandHelpUtility.createCommand("/swarp reload", "Reloads the configuration files",
				sender instanceof Player ? sender.hasPermission("swarp.reload") : true));
		sender.sendMessage(CommandHelpUtility.createCommand("/swarp list [page]", "Lists all warps",
				sender instanceof Player ? sender.hasPermission("swarp.list") : true));
		sender.sendMessage(CommandHelpUtility.createCommand("/swarp create <name>", "Creates a warp",
				sender instanceof Player ? sender.hasPermission("swarp.create") : true));
		sender.sendMessage(
				CommandHelpUtility.createCommand("/swarp create <name> <x> <y> <z>", "Creates a warp at the location",
						sender instanceof Player ? sender.hasPermission("swarp.create") : true));
		sender.sendMessage(CommandHelpUtility.createCommand("/swarp remove <name>", "Removes the warp",
				sender instanceof Player ? sender.hasPermission("swarp.remove") : true));
		sender.sendMessage(CommandHelpUtility.createCommand("/swarp go <name>", "Teleports to the warp",
				sender instanceof Player ? sender.hasPermission("swarp.go") : true));
		sender.sendMessage(
				CommandHelpUtility.createCommand("/swarp point <name>", "Sets your compass to point to the warp",
						sender instanceof Player ? sender.hasPermission("swarp.point") : true));
		sender.sendMessage(CommandHelpUtility.createCommand("/swarp stop", "Makes your compass point to the spawn",
				sender instanceof Player ? sender.hasPermission("swarp.point") : true));
	}

}