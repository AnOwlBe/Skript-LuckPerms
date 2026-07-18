package owlbe.skriptLuckPerms.utilitities;

public class LuckPermsUtils {

	/**
	 * Checks whether the given string is a valid name for a node.
	 * @param string The name to check against.
	 * @return Whether it is a valid node name or not.
	 */
	public static boolean isValidNodeName(String string) {
		return string.matches("[a-z0-9_.-]+");
	}
}
