package com.github.ssuite.swarp.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.ssuite.swarp.Main;
import com.github.ssuite.swarp.service.RequestService;

public class RequestCommand extends AbstractCommand {

	private RequestService requestService;

	public RequestCommand(Main plugin) {
		super(plugin);
		requestService = getPlugin().getRequestService();
	}

	public RequestCommand(Main plugin, String permissionNode) {
		super(plugin, permissionNode);
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			getPlugin().getLogger().severe("Only players can use /swarp request!");
			return true;
		}

		if (args.length != 2) {
			return false;
		}

		if (requestService.hasToken(args[1])) {
			if (args[0].equals("accept")) {
				requestService.acceptRequest(args[1]);
			} else if (args[0].equals("reject")) {
				requestService.rejectRequest(args[1]);
			} else {
				return false;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "The request has already expired!");
		}

		return true;
	}

}
