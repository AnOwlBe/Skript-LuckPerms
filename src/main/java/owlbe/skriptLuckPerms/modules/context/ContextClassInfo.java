package owlbe.skriptLuckPerms.modules.context;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import net.luckperms.api.context.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;
import static owlbe.skriptLuckPerms.utils.PropertyUtils.getProperty;

@SuppressWarnings({"UnstableApiUsage"})
public class ContextClassInfo extends ClassInfo<Context> {

	public ContextClassInfo() {
		super(Context.class, "luckpermscontext");
		this.user("luckperms ?contexts?")
				.name("LuckPerms Context")
				.description("Represents a LuckPerms context.")
				.since("INSERT VERSION")
				.parser(new ContextParser())
				.property(getProperty(TYPED_VALUE, ExpressionPropertyHandler.class),
						"The value of this context.",
						SkriptLuckPerms.getAddonInstance(),
						new ContextValueHandler())
				.defaultExpression(new EventValueExpression<>(Context.class));
	}

	private static class ContextParser extends Parser<Context> {
		//<editor-fold desc="context parser" defaultstate="collapsed">
		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public @Nullable Context parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public String toString(Context context, int flags) {
			return "context with key '" + context.getKey() + "' and value '" + context.getValue() + "'";
		}

		@Override
		public String toVariableNameString(Context context) {
			return context.toString();
		}
		//</editor-fold>
	}

	private static class ContextValueHandler implements TypedValueHandler<Context, String> {
		//<editor-fold desc="context value handler" defaultstate="collapsed">

		@Override
		public @Nullable String convert(Context context) {
			return context.getValue();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

}
