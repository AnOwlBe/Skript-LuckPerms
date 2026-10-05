package owlbe.skriptLuckPerms.modules.context.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.luckperms.api.context.Context;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

@Name("Context Key")
@Description("""
		    Gets the key from the given context.
		    
		    This will be replaced with the key of expression at a later date.
		    """)
@Example("""
		set {_context} to a new luckperms context from key "gamemode" and value "survival"
		set {_key} to context key of {_context} # prints "gamemode"
		""")
@Since("INSERT VERSION")
public class ExprContextKey extends PropertyExpression<Context, String> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprContextKey.class,
						String.class,
						"[luckperm[s]] context key[s]",
						"luckpermscontexts",
						false
				)
						.supplier(ExprContextKey::new)
						.build()
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<Context>) expressions[0]);
		return true;
	}

	@Override
	protected String[] get(Event event, Context[] source) {
		return Arrays.stream(source)
				.map(Context::getKey)
				.toArray(String[]::new);
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "context key of " + getExpr().toString(event, debug);
	}
}
