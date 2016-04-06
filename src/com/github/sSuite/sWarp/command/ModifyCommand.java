package com.github.ssuite.swarp.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.ssuite.slib.utility.CommandHelpUtility;
import com.github.ssuite.swarp.Main;
import com.github.ssuite.swarp.Warp;
import com.github.ssuite.swarp.WarpHandler;
import com.github.ssuite.swarp.exception.NoSuchWarpException;
import com.github.ssuite.swarp.exception.UnsafeWarpNameException;

public class ModifyCommand extends AbstractCommand {

	public ModifyCommand(Main plugin) {
		super(plugin);
	}

	public ModifyCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (args.length != 2 && args.length != 3 && args.length != 5) {
			showHelp(sender);
			return true;
		}

		WarpHandler warpHandler = getPlugin().getWarpHandler();
		Warp targetWarp = null;

		try {
			targetWarp = warpHandler.getWarpByName(args[0]);
		} catch (NoSuchWarpException e) {
			sender.sendMessage(
					ChatColor.RED + "The warp " + ChatColor.RESET + args[0] + ChatColor.RED + " doesn't exist!");
			return true;
		}

		if (!targetWarp.isOwner((Player) sender) && !sender.hasPermission("swarp.modify.all")) {
			sender.sendMessage(ChatColor.RED + "You do not own that warp!");
			return true;
		}

		Location location;

		switch (args[1]) {
			case "name":
				if (args.length != 3) {
					showHelp(sender);
					return true;
				}

				String oldName = targetWarp.getName();

				try {
					targetWarp.setName(args[2]);
				} catch (UnsafeWarpNameException e1) {
					sender.sendMessage(ChatColor.RED
							+ "The warp name must only consist of characters from the character set [A-Za-z0-9-_]!");
				}

				sender.sendMessage(ChatColor.GREEN + "Renamed " + ChatColor.AQUA + oldName + ChatColor.GREEN + " to "
						+ ChatColor.AQUA + targetWarp.getName() + ChatColor.GREEN + "!");

				break;
			case "location":
				if (args.length != 2) {
					location = ((Player) sender).getLocation();
				} else if (args.length != 5) {
					try {
						location = new Location(((Player) sender).getWorld(), Double.parseDouble(args[2]),
								Double.parseDouble(args[3]), Double.parseDouble(args[4]));
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "The coordinates " + args[2] + ", " + args[3] + ", "
								+ args[4] + " are not valid coordinates!");
						return true;
					}
				} else {
					showHelp(sender);
					return true;
				}

				targetWarp.setLocation(location);

				sender.sendMessage(ChatColor.GREEN + "Changed the location of " + ChatColor.AQUA + targetWarp.getName()
						+ ChatColor.GREEN + "!");

				break;
			case "public":
				if (args.length != 2) {
					showHelp(sender);
					return true;
				}

				if (targetWarp.isPublic()) {
					sender.sendMessage(ChatColor.AQUA + targetWarp.getName() + ChatColor.RED + " is already public!");
				} else {
					targetWarp.setPublic(true);
					sender.sendMessage(ChatColor.GREEN + "Made " + ChatColor.AQUA + targetWarp.getName()
							+ ChatColor.GREEN + " a public warp!");
				}

				break;
			case "private":
				if (args.length != 2) {
					showHelp(sender);
					return true;
				}

				if (!targetWarp.isPublic()) {
					sender.sendMessage(ChatColor.AQUA + targetWarp.getName() + ChatColor.RED + " is already private!");
				} else {
					targetWarp.setPublic(false);
					sender.sendMessage(ChatColor.GREEN + "Made " + ChatColor.AQUA + targetWarp.getName()
							+ ChatColor.GREEN + " a private warp!");
				}

				break;
			case "owner":
				if (args.length != 3) {
					showHelp(sender);
					return true;
				}

				OfflinePlayer targetPlayer = null;
				OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();

				for (OfflinePlayer offlinePlayer : offlinePlayers) {
					if (offlinePlayer.getName().equalsIgnoreCase(args[2])) {
						targetPlayer = offlinePlayer;
						break;
					}
				}

				if (targetPlayer == null) {
					sender.sendMessage(
							ChatColor.RED + "That player has never logged in to the server and so could not be found!");
					return true;
				}

				targetWarp.setOwner(targetPlayer);

				// sender.sendMessage(ChatColor.YELLOW + "Are you sure you want
				// to change the owner of " + ChatColor.AQUA
				// + targetWarp.getName() + ChatColor.YELLOW + " to " +
				// ChatColor.GOLD + targetPlayer.getName()
				// + ChatColor.YELLOW + "? Repeat the command to confirm.");

				sender.sendMessage(ChatColor.GREEN + "Changed the owner of " + ChatColor.AQUA + targetWarp.getName()
						+ ChatColor.GREEN + " to " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + "!");

				break;
			case "pitch":
				if (args.length != 3) {
					showHelp(sender);
					return true;
				}

				location = targetWarp.getLocation();
				float pitch = (float) Double.parseDouble(args[2]);

				if (pitch < -90 || pitch > 90) {
					sender.sendMessage(ChatColor.RED + "Yaw must be between -90 and 90!");
					return true;
				}

				location.setPitch(pitch);
				targetWarp.setLocation(location);

				sender.sendMessage(ChatColor.GREEN + "Changed the pitch of " + ChatColor.AQUA + targetWarp.getName()
						+ ChatColor.GREEN + " to " + ChatColor.GOLD + pitch + ChatColor.GREEN + "!");

				break;
			case "yaw":
				if (args.length != 3) {
					showHelp(sender);
					return true;
				}

				location = targetWarp.getLocation();
				float yaw = (float) Double.parseDouble(args[2]);

				if (yaw < -180 || yaw > 180) {
					sender.sendMessage(ChatColor.RED + "Yaw must be between -180 and 180!");
					return true;
				}

				location.setYaw(yaw);
				targetWarp.setLocation(location);

				sender.sendMessage(ChatColor.GREEN + "Changed the yaw of " + ChatColor.AQUA + targetWarp.getName()
						+ ChatColor.GREEN + " to " + ChatColor.GOLD + yaw + ChatColor.GREEN + "!");

				break;
			default:
				showHelp(sender);
		}

		return true;
	}

	public void showHelp(CommandSender sender) {
		CommandHelpUtility.sendHeader("/swarp modify Help", sender);
		CommandHelpUtility.sendCommand("/swarp modify <name> name <newName>", "Sets the name of the warp", sender);
		CommandHelpUtility.sendCommand("/swarp modify <name> location", "Sets the location of the warp", sender);
		CommandHelpUtility.sendCommand("/swarp modify <name> location <x> <y> <z>", "Sets the location of the warp",
				sender);
		CommandHelpUtility.sendCommand("/swarp modify <name> public", "Makes the warp public", sender);
		CommandHelpUtility.sendCommand("/swarp modify <name> private", "Makes the warp private", sender);
		CommandHelpUtility.sendCommand("/swarp modify <name> owner <player>", "Sets the owner of the warp", sender);
		CommandHelpUtility.sendCommand("/swarp modify <name> pitch <pitch>", "Sets the pitch, in degrees, of the warp",
				sender);
		CommandHelpUtility.sendCommand("/swarp modify <name> yaw <yaw>", "Sets the yaw, in degrees, of the warp",
				sender);
	}

}
