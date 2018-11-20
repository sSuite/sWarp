package com.github.ssuite.swarp;

import com.github.ssuite.slib.utility.CommandHelpUtility;
import com.github.ssuite.swarp.command.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
	
	private static final char FLAG_CHARACTER = '-';
	
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
			case "invite":
				commandClass = new InviteCommand(plugin, "invite");
				break;
			case "uninvite":
				commandClass = new UninviteCommand(plugin, "invite");
				break;
			case "edit":
				commandClass = new ModifyCommand(plugin, "modify");
				break;
			case "go":
				commandClass = new GoCommand(plugin, "go");
				break;
			case "point":
				commandClass = new PointCommand(plugin, "point");
				break;
			case "info":
				commandClass = new InfoCommand(plugin, "info");
				break;
			case "player":
				commandClass = new PlayerCommand(plugin, "player");
				break;
			case "request":
				commandClass = new RequestCommand(plugin);
				break;
			default:
				showHelp(sender);
				return true;
		}
		
		newArgs = commandClass.doFlagProcessing(newArgs, FLAG_CHARACTER);
		
		if (!commandClass.execute(sender, newArgs)) {
			showHelp(sender);
		}
		return true;
	}
	
	private void showHelp(CommandSender sender) {
		CommandHelpUtility.sendHeader("sWarp Help", sender);
		CommandHelpUtility.sendCommand("/swarp reload", "Reloads the configuration files", sender, "swarp.reload");
		CommandHelpUtility.sendCommand("/swarp list [page]", "Lists the warps", sender, "swarp.list");
		CommandHelpUtility.sendCommand("/swarp create [-p|--private] <name> [x] [y] [z]",
				"Creates a warp at your current location or the location specified. The warp is public unless -p or --private is specified",
				sender, "swarp.create");
		CommandHelpUtility.sendCommand("/swarp remove <name>", "Removes the named warp", sender, "swarp.remove");
		CommandHelpUtility.sendCommand("/swarp invite <player> <name>", "Invites the player to the named warp", sender, "swarp.invite");
		CommandHelpUtility.sendCommand("/swarp uninvite <player> <name>", "Uninvites the player from the named warp", sender,
				"swarp.invite");
		CommandHelpUtility.sendCommand("/swarp edit <name>", "Edits the named warp", sender, "swarp.modify");
		CommandHelpUtility.sendCommand("/swarp go <name>", "Teleports to the named warp", sender, "swarp.go");
		CommandHelpUtility.sendCommand("/swarp point [name]",
				"Sets your compass to point to the named warp or resets it if no warp is specified", sender, "swarp.point");
		CommandHelpUtility.sendCommand("/swarp info <name>", "Provides information about the named warp", sender, "swarp.info");
		CommandHelpUtility.sendCommand("/swarp player [-s|--silent] <player>",
				"Sets your compass to point to the player, if the player accepts the request. If -s or --silent is specified, tracks the player without a request",
				sender, "swarp.player");
	}
	
}
