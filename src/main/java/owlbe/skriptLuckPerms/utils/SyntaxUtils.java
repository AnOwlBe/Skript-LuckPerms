package owlbe.skriptLuckPerms.utils;

import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

/**
 * Utils for working with syntaxes (primarily getting the amount of registered syntaxes)
 */
public final class SyntaxUtils {

	public static final SyntaxRegistry.Key<? extends SyntaxInfo<?>>[] SYNTAX_KEYS = new SyntaxRegistry.Key<?>[] {
			SyntaxRegistry.STRUCTURE,
			BukkitSyntaxInfos.Event.KEY,
			SyntaxRegistry.SECTION,
			SyntaxRegistry.EFFECT,
			SyntaxRegistry.EXPRESSION,
			SyntaxRegistry.CONDITION,
	};

	private SyntaxUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	/**
	 * Gets the total amount of syntaxes registered by this addon.
	 *
	 * @param addon The addon to get the elements from
	 * @return The amount registered
	 */
	public static long getTotalAmount(SkriptAddon addon) {
		SyntaxRegistry syntaxRegistry = addon.syntaxRegistry();

		long amount = syntaxRegistry.elements().stream()
				.filter(syntaxInfo -> syntaxInfo.origin().name().equals(addon.name()))
				.count();

		amount = amount + getPropertyAmount(addon); // for properties
		amount = amount + 14; // For types

		return amount;
	}

	/**
	 * Gets the total amount of registered syntaxes of the given key.
	 *
	 * @param addon The addon to get the elements from
	 * @param key The syntax key
	 * @return The amount of the given key registered
	 */
	public static long getAmount(SkriptAddon addon, SyntaxRegistry.Key<? extends SyntaxInfo<?>> key) {
		return addon.syntaxRegistry().syntaxes(key).stream()
				.filter(syntaxInfo -> syntaxInfo.origin().name().equals(addon.name()))
				.count();
	}

	/**
	 * Gets the total amount of registered properties from this addon.
	 *
	 * @param addon The addon to compare against and get the registry from
	 * @return The total amount of properties registered by the given addon
	 */
	@SuppressWarnings("UnstableApiUsage")
	public static long getPropertyAmount(SkriptAddon addon) {
		PropertyRegistry propertyRegistry = addon.registry(PropertyRegistry.class);

		long amount = 0;

		for (Property<?> property : propertyRegistry) {
			if (property.provider().equals(addon))
				amount++;
		}

		return amount;
	}

	/**
	 * Formats a syntax key from e.g. 'event' -> 'Event'
	 * if the amount is higher than 1 will return plural form (e.g. 'Events')
	 *
	 * @param key The syntax key to format
	 * @param amount The amount of the given syntax key registered
	 * @return The formatted syntax key
	 */
	public static String formatKeyName(SyntaxRegistry.Key<? extends SyntaxInfo<?>> key, long amount) {
		String name = key.name();
		String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

		if (amount == 1)
			return capitalized;

		if (capitalized.endsWith("y"))
			return capitalized.substring(0, capitalized.length() - 1) + "ies";

		return capitalized + "s";
	}

}
