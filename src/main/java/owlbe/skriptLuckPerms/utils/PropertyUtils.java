package owlbe.skriptLuckPerms.utils;

import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyRegistry;
import org.skriptlang.skript.lang.properties.handlers.base.PropertyHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;

/**
 * Utils for working with {@link Property}s
 */
@SuppressWarnings("UnstableApiUsage")
public final class PropertyUtils {

	private PropertyUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <Handler extends PropertyHandler<?>> @Nullable Property<Handler> getProperty(Property<Handler> property, Class<? extends PropertyHandler> clazz) {
		PropertyRegistry propertyRegistry = SkriptLuckPerms.getAddonInstance().registry(PropertyRegistry.class);

		if (!propertyRegistry.isRegistered(property)) {
			propertyRegistry.register(property);
			return property;
		}

		Property<?> otherProperty = propertyRegistry.get(property.name());

		if (clazz.isAssignableFrom(otherProperty.handler())) {
			return (Property<Handler>) otherProperty;
		} else {
			return null;
		}

	}

}
