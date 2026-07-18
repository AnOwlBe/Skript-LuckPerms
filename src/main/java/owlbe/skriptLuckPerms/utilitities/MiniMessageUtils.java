package owlbe.skriptLuckPerms.utilitities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniMessageUtils {

	/**
	 * Quick method to make formatting a string easier.
	 * @param message The raw string to format
	 * @return A formatted text component from the given string
	 */
	public static Component minimessage(String message) {
		return MiniMessage.miniMessage().deserialize(message);
	}

}
