package owlbe.skriptLuckPerms.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public final class MiniMessageUtils {

	public static final String PREFIX = "<reset><dark_gray>[<shadow:#63FFA4:0.3><gradient:#63FFA4:#9CFFC5:#63FFA4><b>SKRIPT-LUCKPERMS<reset><dark_gray>]<reset>";

	private MiniMessageUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	/**
	 * Quick method to make formatting a string easier.
	 *
	 * @param message The raw string to format
	 * @return A formatted text component from the given string
	 */
	public static Component minimessage(String message) {
		message = message.replace("%prefix%", PREFIX);
		return MiniMessage.miniMessage().deserialize(message);
	}

	/**
	 * Sends a message to the given command sender.
	 *
	 * @param sender The sender to send the message to
	 */
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(minimessage(message));
	}

}
