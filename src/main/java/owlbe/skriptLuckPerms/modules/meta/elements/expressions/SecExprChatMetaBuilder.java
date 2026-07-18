package owlbe.skriptLuckPerms.modules.meta.elements.expressions;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import net.luckperms.api.node.types.ChatMetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

@Name("Chat Node Builder")
@Description("""
		Builds a prefix/suffix with the given values.
		
		You can add said prefix/suffix to a user or a group via the prefix or suffix expression.
		""")
@Example("""
command /test:
	trigger:
		set {_lp} to luckperms user from player
		set {_m} to a new suffix builder:
			value: "NERD"
			priority: 5
			duration: 1 week
		add {_m} to suffixes of {_lp}
		""")
@Example("""
command /test:
	trigger:
		set {_lp} to luckperms user from player
		set {_m} to a new prefix builder:
			value: "<red>ADMIN"
			priority: 255
			duration: 1 hour
		add {_m} to prefixes of {_lp}
	""")
@Since("1.0")
@SuppressWarnings("rawtypes")
public class SecExprChatMetaBuilder extends SectionExpression<ChatMetaNode> {

	private static EntryValidator VALIDATOR;

	public static void register(SyntaxRegistry syntaxRegistry) {
		VALIDATOR = EntryValidator.builder()
				.addEntryData(new ExpressionEntryData<>("value", null, false, String.class))
				.addEntryData(new ExpressionEntryData<>("priority", null, false, Integer.class))
				.addEntryData(new ExpressionEntryData<>("duration", null, true, Timespan.class))
				.build();

		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(SecExprChatMetaBuilder.class, ChatMetaNode.class)
						.addPattern("a [new] (prefix|suffix) builder")
						.build()
		);
	}

	private Expression<String> value;
	private Expression<Integer> priority;
	private Expression<Timespan> duration;
	private String type;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> list) {
		type = parseResult.expr.contains("prefix") ? "prefix" : "suffix";

		EntryContainer container = null;
		if (sectionNode != null)
			container = VALIDATOR.validate(sectionNode);
		if (container == null)
			return false;

		value = (Expression<String>) container.getOptional("value", false);
		priority = (Expression<Integer>) container.getOptional("priority", false);
		duration = (Expression<Timespan>) container.getOptional("duration", false);
		return true;
	}

	@Override
	protected ChatMetaNode @Nullable [] get(Event event) {
		String value = this.value != null ? this.value.getSingle(event) : "";
		if (value == null)
			return new ChatMetaNode[0];

		int priority = this.priority != null ? this.priority.getSingle(event) : 0;
		Timespan duration = this.duration != null ? this.duration.getSingle(event) : null;

		if (type.equals("prefix")) {
			PrefixNode.Builder builder = PrefixNode.builder(value, priority);
			if (duration != null)
				builder.expiry(duration);
			return new ChatMetaNode[]{builder.build()};
		} else {
			SuffixNode.Builder builder = SuffixNode.builder(value, priority);
			if (duration != null)
				builder.expiry(duration);
			return new ChatMetaNode[]{builder.build()};
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends ChatMetaNode> getReturnType() {
		return ChatMetaNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "a" + type + "builder";
	}

}
