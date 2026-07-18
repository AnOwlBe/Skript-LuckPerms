package owlbe.skriptLuckPerms.skript.properties;

import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyRegistry;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.utilitities.Logger;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;

@SuppressWarnings({"UnstableApiUsage", "rawtypes"})
public class Properties {

	/**
	 * A property for getting the weight of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> WEIGHT = Property.of(
			"weight",
			"The weight of something.",
			"1.0.3",
			addon,
			ExpressionPropertyHandler.class);


	/**
	 * A property for getting the priority of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> PRIORITY = Property.of(
			"priority",
			"The priority of something.",
			"1.0.3",
			addon,
			ExpressionPropertyHandler.class);

	/**
	 * A property for getting the source of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> SOURCE = Property.of(
			"source",
			"The source of something.",
			"1.0.3",
			addon,
			ExpressionPropertyHandler.class);

	/**
	 * A property for getting the expiry of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> EXPIRY = Property.of(
			"expiry",
			"The expiry of something.",
			"INSERT VERSION",
			addon,
			ExpressionPropertyHandler.class);

	public static void register(SyntaxRegistry syntaxRegistry) {
		if (getProperty(WEIGHT) != null) {
			PropExprWeight.register(syntaxRegistry);
		} else {
			Logger.error("It appears another addon tried to register a WEIGHT property & Skript-LuckPerms failed to hook into it. Disabling Skript-LuckPerms usages of WEIGHT property.");
		}
		if (getProperty(PRIORITY) != null) {
			PropExprPriority.register(syntaxRegistry);
		} else {
			Logger.error("It appears another addon tried to register a PRIORITY property & Skript-LuckPerms failed to hook into it. Disabling Skript-LuckPerms usages of PRIORITY property.");
		}
		if (getProperty(SOURCE) != null) {
			PropExprSource.register(syntaxRegistry);
		} else {
			Logger.error("It appears another addon tried to register a SOURCE property & Skript-LuckPerms failed to hook into it. Disabling Skript-LuckPerms usages of SOURCE property.");
		}
		if (getProperty(EXPIRY) != null) {
			PropExprExpiry.register(syntaxRegistry);
		} else {
			Logger.error("It appears another addon tried to register a EXPIRY property & Skript-LuckPerms failed to hook into it. Disabling Skript-LuckPerms usages of EXPIRY property.");
		}
	}

	public static Property getProperty(Property<?> property) {
		PropertyRegistry propertyRegistry = addon.registry(PropertyRegistry.class);
		if (propertyRegistry.isRegistered(property)) {
			Property<?> property2 = propertyRegistry.get(property.name());
			if (ExpressionPropertyHandler.class.isAssignableFrom(property2.handler())) {
	          return property2;
			} else {
				return null;
			}

		} else {
			propertyRegistry.register(property);
			return property;
		}
	}

}
