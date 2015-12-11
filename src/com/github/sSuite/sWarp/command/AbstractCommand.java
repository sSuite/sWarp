package com.github.sSuite.sWarp.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.sSuite.sWarp.Main;

public abstract class AbstractCommand {

	private static final String PERMISSION_ROOT = "swarp.";

	private Main plugin;
	private ArrayList<String> flags;
	private String permissionNode;

	public AbstractCommand(Main plugin) {
		this.plugin = plugin;
	}

	public AbstractCommand(Main plugin, String permissionNode) {
		this.plugin = plugin;
		this.permissionNode = permissionNode;
	}

	public final String[] doFlagProcessing(String[] args, char flagCharacter) {
		String[] arguments = preProcessFlags(args, flagCharacter);

		flags = processFlags();

		return arguments;
	}

	/**
	 * Individual flags are characterized as any alphabetic character
	 * immediately preceeded by the <code>flagCharacter</code>. Multiple flags
	 * can be specified with only one <code>flagCharacter</code> if they
	 * immediately follow the alphabetic character. Processes any flags found in
	 * the command arguments, stores them internally, and returns the arguments
	 * without any flags.
	 *
	 * @param args
	 *            - the arguments
	 * @param flagCharacter
	 *            - the character that indicates a flag
	 * @return the arguments without any flags
	 */
	private final String[] preProcessFlags(String[] args, char flagCharacter) {
		ArrayList<String> flags = new ArrayList<String>();
		ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(args));

		for (int i = args.length - 1; i >= 0; i--) {
			Matcher singleFlagMatcher = Pattern.compile("^-([A-Za-z]+)$").matcher(args[i]);
			Matcher multiFlagMatcher = Pattern.compile("^--([A-Za-z]+)$").matcher(args[i]);

			if (singleFlagMatcher.find()) {
				String flagCharacters = singleFlagMatcher.group(1);

				for (int j = 0; j < flagCharacters.length(); j++) {
					flags.add(flagCharacters.charAt(j) + "");
				}

				arguments.remove(i);
			} else if (multiFlagMatcher.find()) {
				flags.add(multiFlagMatcher.group(1));

				arguments.remove(i);
			}
		}

		this.flags = flags;

		return arguments.toArray(new String[arguments.size()]);
	}

	/**
	 * Further process the flags.
	 *
	 * @return the new flags
	 */
	public ArrayList<String> processFlags() {
		return flags;
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
	public final boolean execute(CommandSender sender, String[] args) {
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

	/**
	 * @return the flags sent with the command
	 */
	public ArrayList<String> getFlags() {
		ArrayList<String> flagsCopy = new ArrayList<String>();
		flagsCopy.addAll(flags);

		return flagsCopy;
	}

}
