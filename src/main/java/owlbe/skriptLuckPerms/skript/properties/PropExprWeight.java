package owlbe.skriptLuckPerms.skript.properties;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.utils.PropertyUtils;

import static owlbe.skriptLuckPerms.skript.properties.Properties.WEIGHT;

@Name("Weight")
@Description("""
	Represents the weight of something.
	""")
@Example("set {_weight} to the weight of {_mygroup}")
@Since("1.0.3-BETA")
@RelatedProperty("weight")
@SuppressWarnings("UnstableApiUsage")
public class PropExprWeight extends PropertyBaseExpression<ExpressionPropertyHandler<?, ?>> {

	public static void register(SyntaxRegistry registry) {
		registry.register(SyntaxRegistry.EXPRESSION,
				PropertyExpression.infoBuilder(PropExprWeight.class, Object.class, "weight", "objects", false)
						.supplier(PropExprWeight::new)
						.build());
	}

	@Override
	@SuppressWarnings({"DataFlowIssue"}) // won't be null
	public @NotNull Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return PropertyUtils.getProperty(WEIGHT, ExpressionPropertyHandler.class);
	}

}
