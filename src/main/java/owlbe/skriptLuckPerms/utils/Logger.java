package owlbe.skriptLuckPerms.utils;

import org.bukkit.Bukkit;

import static owlbe.skriptLuckPerms.utils.MiniMessageUtils.minimessage;

public final class Logger {

	private Logger() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	private static final String PREFIX = "<#63FFA4>Skript-LuckPerms <reset><dark_gray>→ ";

	/**
	 * Prints the given message to console with Skript-LuckPerm's formatting.
	 * @param message The message to print
	 */
	public static void fine(String message) {
		Bukkit.getConsoleSender().sendMessage(minimessage(PREFIX + "<gray>" + message));
	}

	/**
	 * Prints a warning with the given message to console with Skript-LuckPerm's formatting.
	 * @param message The message to print
	 */
	public static void warning(String message) {
		Bukkit.getConsoleSender().sendMessage(minimessage(PREFIX + "<#F4E09F>" + message));

	}

	/**
	 * Prints an error with the given message to console with Skript-LuckPerm's formatting.
	 * @param message The message to print
	 */
	public static void error(String message) {
		Bukkit.getConsoleSender().sendMessage(minimessage(PREFIX + "<#FF5252>" + message));
	}

}
