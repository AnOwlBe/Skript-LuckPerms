package owlbe.skriptLuckPerms.skript.properties;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.PropertyExpression;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.PropertyBaseExpression;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

import static owlbe.skriptLuckPerms.skript.properties.Properties.SOURCE;

@Name("Source")
@Description("""
	Represents the source of something.
	""")
@Example("set {_source} to the source of {_chatmeta}")
@Since("1.0.3")
@RelatedProperty("source")
@SuppressWarnings("UnstableApiUsage")
public class PropExprSource extends PropertyBaseExpression<ExpressionPropertyHandler<?, ?>> {

	public static void register(SyntaxRegistry registry) {
		registry.register(SyntaxRegistry.EXPRESSION,
				PropertyExpression.infoBuilder(PropExprSource.class, Object.class, "source", "objects", false)
						.supplier(PropExprSource::new)
						.build());
	}

	@Override
	public Property<ExpressionPropertyHandler<?, ?>> getProperty() {
		return Properties.getProperty(SOURCE);
	}

}
