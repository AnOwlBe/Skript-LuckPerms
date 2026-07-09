package owlbe.skriptLuckPerms.skript.properties;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

import static owlbe.skriptLuckPerms.skript.properties.Properties.PRIORITY;

@SuppressWarnings("UnstableApiUsage")
@Name("Priority")
@Description("""
		Represents the priority of something.
		""")
@Example("set {_m} to the priority of {_chatmeta}")
@Since("1.0.3")
@RelatedProperty("priority")
public class PropExprPriority extends PropertyBaseExpression<ExpressionPropertyHandler<?, ?>> {

	public static void register(SyntaxRegistry registry) {
		registry.register(SyntaxRegistry.EXPRESSION,
				PropertyExpression.infoBuilder(PropExprPriority.class, Object.class, "priority", "objects", false)
						.supplier(PropExprPriority::new)
						.build());
	}

	@Override
	public Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return Properties.getProperty(PRIORITY);
	}

}
