package owlbe.skriptLuckPerms.skript.properties;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

import static owlbe.skriptLuckPerms.skript.properties.Properties.EXPIRY;

@SuppressWarnings("UnstableApiUsage")
@Name("Expiry")
@Description("""
		Represents when something will expire.
		""")
@Example("set {_m} to the expiry of {_chatmeta}")
@Since("INSERT VERSION")
@RelatedProperty("expiry")
public class PropExprExpiry extends PropertyBaseExpression<ExpressionPropertyHandler<?, ?>> {

	public static void register(SyntaxRegistry registry) {
		registry.register(SyntaxRegistry.EXPRESSION,
				PropertyExpression.infoBuilder(PropExprExpiry.class, Object.class, "expiry", "objects", false)
						.supplier(PropExprExpiry::new)
						.build());
	}

	@Override
	public Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return Properties.getProperty(EXPIRY);
	}

}
