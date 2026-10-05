package owlbe.skriptLuckPerms.modules.context.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.context.Context;
import net.luckperms.api.context.ImmutableContextSet;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Set;

@Name("Context From Key And Value")
@Description("""
		    Creates a  context from the given key and value.
		    """)
@Example("""
		command /flight:
		    trigger:
		        set {_context} to a luckperms context from key "gamemode" and value "survival"
		        set {_node} to a new luckperms permission from key "perk.flight":
		            set context of event-permission to {_context}
		        set {_user} to luckperms user from player
		        edit luckperms user {_user}:
		            add {_node} to luckperms prefixes of event-user
		        # User now has flight perk only while in survival!
		""")
@Since("INSERT VERSION")
public class ExprContext extends SimpleExpression<Context> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprContext.class, Context.class)
						.addPatterns("[a] [new] luckperm[s] context (with|from) key %string% and value %string%")
						.supplier(ExprContext::new)
						.build()
		);
	}

	private Expression<String> key;
	private Expression<String> value;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		key = (Expression<String>) expressions[0];
		value = (Expression<String>) expressions[1];
		return true;
	}

	@Override
	protected Context @Nullable [] get(Event event) {
		String key = this.key.getSingle(event);
		String value = this.value.getSingle(event);
		if (key == null || value == null)
			return new Context[0];

		Set<Context> contexts = ImmutableContextSet.of(key, value).toSet(); // cannot build a singular context
		Context context = contexts.iterator().next();
		return new Context[] {context};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Context> getReturnType() {
		return Context.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "a new luckperms context from key '" + key.toString(event, debug) + "' and value '" + value.toString(event, debug) + "'";
	}

}
