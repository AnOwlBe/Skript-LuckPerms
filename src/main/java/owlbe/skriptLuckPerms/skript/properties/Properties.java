package owlbe.skriptLuckPerms.skript.properties;

import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.utils.Logger;
import owlbe.skriptLuckPerms.utils.PropertyUtils;

import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"UnstableApiUsage"})
public class Properties {

	/**
	 * A property for getting the weight of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> WEIGHT = Property.of(
			"weight",
			"The weight of something.",
			"1.0.3-BETA",
			SkriptLuckPerms.getAddonInstance(),
			ExpressionPropertyHandler.class);


	/**
	 * A property for getting the priority of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> PRIORITY = Property.of(
			"priority",
			"The priority of something.",
			"1.0.3-BETA",
			SkriptLuckPerms.getAddonInstance(),
			ExpressionPropertyHandler.class);

	/**
	 * A property for getting the source of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> SOURCE = Property.of(
			"source",
			"The source of something.",
			"1.0.3-BETA",
			SkriptLuckPerms.getAddonInstance(),
			ExpressionPropertyHandler.class);

	/**
	 * A property for getting the expiry of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> EXPIRY = Property.of(
			"expiry",
			"The expiry of something.",
			"INSERT VERSION",
			SkriptLuckPerms.getAddonInstance(),
			ExpressionPropertyHandler.class);

	/**
	 * A property for getting the context of something.
	 */
	public static final Property<ExpressionPropertyHandler<?,?>> CONTEXT = Property.of(
			"context",
			"The context of something.",
			"INSERT VERSION",
			SkriptLuckPerms.getAddonInstance(),
			ExpressionPropertyHandler.class);

	private static final Map<Property<?>, Consumer<SyntaxRegistry>> PROPERTIES = Map.of(
			WEIGHT, PropExprWeight::register,
			PRIORITY, PropExprPriority::register,
			SOURCE, PropExprSource::register,
			EXPIRY, PropExprExpiry::register,
			CONTEXT, PropExprContext::register
	);

	// TODO: In future there may not just be expression type properties - make this support conditions, effects etc
	public static void register(SyntaxRegistry syntaxRegistry) {
		PROPERTIES.forEach((property, registrar) -> {
			if (PropertyUtils.getProperty(property, ExpressionPropertyHandler.class) != null) {
				registrar.accept(syntaxRegistry);
			} else {
				Logger.error("It appears another addon tried to register a " + property + " property & Skript-LuckPerms failed to hook into it. Disabling Skript-LuckPerms usages of " + property + " property.");
			}
		});
	}

}
