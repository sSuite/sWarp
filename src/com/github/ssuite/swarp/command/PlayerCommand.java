package com.github.ssuite.swarp.command;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.ssuite.slib.exception.AmbiguousPlayerNameException;
import com.github.ssuite.slib.exception.NoSuchPlayerException;
import com.github.ssuite.slib.utility.PlayerUtility;
import com.github.ssuite.swarp.Main;

public class PlayerCommand extends AbstractCommand {

	public PlayerCommand(Main plugin) {
		super(plugin);
	}

	public PlayerCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public ArrayList<String> processFlags() {
		ArrayList<String> flags = getFlags();

		for (String flag : flags) {
			if (flag.equals("silent")) {
				if (!flags.contains("s")) {
					flags.add("s");
				}

				flags.remove("silent");
			}
		}

		return flags;
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().getLogger().severe("Only players can use /swarp player!");
			return true;
		}

		if (args.length > 1) {
			return false;
		}

		if (args.length == 0) {
			getPlugin().getPlayerLocationService().cancelUpdate((Player) sender);

			sender.sendMessage(ChatColor.GREEN + "Stopped tracking!");
		} else {
			Player targetPlayer;
			try {
				targetPlayer = PlayerUtility.getOnlinePlayerByName(args[0]);

				if (!targetPlayer.getWorld().equals(((Player) sender).getWorld())) {
					sender.sendMessage(ChatColor.RED + "That player is in a different world!");
					return true;
				}

				if (getFlags().contains("s")) {
					if (hasSubPermission(sender, "silent")) {
						getPlugin().getPlayerLocationService().registerUpdate((Player) sender, targetPlayer);
						sender.sendMessage(
								"Use " + ChatColor.GOLD + "/swarp player" + ChatColor.RESET + " to stop tracking "
										+ ChatColor.GOLD + targetPlayer.getName() + ChatColor.RESET + ".");
					} else {
						sender.sendMessage(
								ChatColor.RED + "You do not have sufficient permissions to track players silently!");
					}
				} else {
					getPlugin().getPlayerLocationService().requestTrack((Player) sender, targetPlayer);
					sender.sendMessage("Sent " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.RESET
							+ " a tracking request.");
				}
			} catch (AmbiguousPlayerNameException e) {
				sender.sendMessage(e.getFormattedMessage());
			} catch (NoSuchPlayerException e) {
				sender.sendMessage(e.getFormattedMessage());
			}
		}

		return true;
	}

}
