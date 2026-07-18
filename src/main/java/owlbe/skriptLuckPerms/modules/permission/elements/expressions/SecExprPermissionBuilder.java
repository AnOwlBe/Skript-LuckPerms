package owlbe.skriptLuckPerms.modules.permission.elements.expressions;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.time.Duration;
import java.util.List;

@Name("Permission Builder")
@Description("""
		Builds a permission with the given values.
		
		`duration` = How long the permission will last for.
		`context` = The context needed for this permission to apply.
		
		Note that you only need this if you want a permission that has expiry and/or context.
		""")
@Example("""

""")
@Since("INSERT VERSION")
public class SecExprPermissionBuilder extends SectionExpression<PermissionNode> {

	private static EntryValidator VALIDATOR;

	public static void register(SyntaxRegistry syntaxRegistry) {
		VALIDATOR = EntryValidator.builder()
				.addEntryData(new ExpressionEntryData<>("duration", null, true, Timespan.class))
				.addEntryData(new ExpressionEntryData<>("context", null, true, ContextSet.class))
				.build();

		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(SecExprPermissionBuilder.class, PermissionNode.class)
						.addPattern("a [new] perm[ission] builder (with|from) (key|id) %string%")
						.build()
		);
	}

	private Expression<String> key = null;
	private Expression<Timespan> duration = null;
	private Expression<ContextSet> context = null;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
		key = (Expression<String>) expressions[0];
		if (sectionNode != null) {
			EntryContainer container = VALIDATOR.validate(sectionNode);
			if (container == null)
				return false;
			duration = (Expression<Timespan>) container.getOptional("duration", false);
			context = (Expression<ContextSet>) container.getOptional("context", false);
		}
		return true;
	}

	@Override
	protected PermissionNode @Nullable [] get(Event event) {
		String key = this.key.getSingle(event);
		if (key == null)
			return new PermissionNode[0];

		Timespan duration = Timespan.fromDuration(Duration.ZERO);
		if (this.duration != null)
			duration = this.duration.getSingle(event);
		if (duration == null)
			return new PermissionNode[0];

		ContextSet context = ImmutableContextSet.empty();
		if (this.context != null)
			context = this.context.getSingle(event);
		if (context == null)
			return new PermissionNode[0];

		PermissionNode permissionNode = PermissionNode.builder(key)
				.expiry(duration)
				.context(context)
				.build();

		return new PermissionNode[]{permissionNode};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends PermissionNode> getReturnType() {
		return PermissionNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("a permission builder from key", key)
				.toString();
	}

}
