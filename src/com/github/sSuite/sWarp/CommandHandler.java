package com.github.sSuite.sWarp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.github.sSuite.sLib.utility.CommandHelpUtility;
import com.github.sSuite.sWarp.command.AbstractCommand;
import com.github.sSuite.sWarp.command.CreateCommand;
import com.github.sSuite.sWarp.command.GoCommand;
import com.github.sSuite.sWarp.command.InfoCommand;
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
			case "info":
				commandClass = new InfoCommand(plugin, "info");
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
		CommandHelpUtility.sendHeader("sWarp Help", sender);
		CommandHelpUtility.sendCommand("/swarp reload", "Reloads the configuration files", sender, "swarp.reload");
		CommandHelpUtility.sendCommand("/swarp list [page]", "Lists the warps", sender, "swarp.list");
		CommandHelpUtility.sendCommand("/swarp create <name>", "Creates a warp at your current location", sender,
				"swarp.create");
		CommandHelpUtility.sendCommand("/swarp create <name> <x> <y> <z>", "Creates a warp at the location", sender,
				"swarp.create");
		CommandHelpUtility.sendCommand("/swarp remove <name>", "Removes the named warp", sender, "swarp.remove");
		CommandHelpUtility.sendCommand("/swarp go <name>", "Teleports to the named warp", sender, "swarp.go");
		CommandHelpUtility.sendCommand("/swarp point <name>", "Sets your compass to point to the named warp", sender,
				"swarp.point");
		CommandHelpUtility.sendCommand("/swarp stop", "Makes your compass point to the spawn", sender, "swarp.point");
		CommandHelpUtility.sendCommand("/swarp info <name>", "Provides information about the named warp", sender,
				"swarp.info");
	}

}
