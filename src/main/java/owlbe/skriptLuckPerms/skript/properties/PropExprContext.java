package owlbe.skriptLuckPerms.skript.properties;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import org.jspecify.annotations.NonNull;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

import static owlbe.skriptLuckPerms.skript.properties.Properties.CONTEXT;

@Name("Context")
@Description("""
	Represents the context of something..
	""")
@Example("set context of {_meta} to {_mycoolcontext}")
@Since("INSERT VERSION")
@RelatedProperty("context")
@SuppressWarnings("UnstableApiUsage")
public class PropExprContext extends PropertyBaseExpression<ExpressionPropertyHandler<?, ?>> {

	public static void register(SyntaxRegistry registry) {
		registry.register(SyntaxRegistry.EXPRESSION,
				PropertyExpression.infoBuilder(PropExprContext.class, Object.class, "context[s]", "objects", false)
						.supplier(PropExprContext::new)
						.build());
	}

	@Override
	@SuppressWarnings("DataFlowIssue") // won't be null
	public @NonNull Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return Properties.getProperty(CONTEXT);
	}

}
