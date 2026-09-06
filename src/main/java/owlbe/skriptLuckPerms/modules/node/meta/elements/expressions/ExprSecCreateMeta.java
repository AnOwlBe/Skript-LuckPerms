package owlbe.skriptLuckPerms.modules.node.meta.elements.expressions;


import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.utils.wrapper.MetaNodeWrapper;

import java.util.List;

@Name("Custom Meta")
@Description("""
		Creates a custom meta with the given values.
		
		See <the ref> for how to add a prefix or suffix to a permission holder.
		""")
@Example("""
        set {_meta} to a luckperms meta with key "wins" and value "5":
            set expiry of event-meta to 4 hours
        """)
@Since("INSERT VERSION")
public class ExprSecCreateMeta extends SectionExpression<MetaNode> {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprSecCreateMeta.class, MetaNode.class)
						.addPatterns("[a] [new] luckperm[s] meta [node] with key %string% and value %string%")
						.supplier(ExprSecCreateMeta::new)
						.build()
		);

		eventValueRegistry.register(EventValue.builder(MetaSectionEvent.class, MetaNodeWrapper.class)
				.getter(MetaSectionEvent::getNode)
				.patterns("meta [node]")
				.build());
	}

	private Expression<String> key;
	private Expression<String> value;
	private @Nullable Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItem) {
		key = (Expression<String>) expressions[0];
		value = (Expression<String>) expressions[1];

		if (sectionNode != null) {
			trigger = SectionUtils.loadLinkedCode("meta node section", (beforeLoading, afterLoading)
					-> loadCode(sectionNode, "meta node section", beforeLoading, afterLoading, MetaSectionEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected MetaNode @Nullable [] get(Event event) {
		String key = this.key.getSingle(event);
		String value = this.value.getSingle(event);

		MetaNodeWrapper wrapper = new MetaNodeWrapper(key, value);

		MetaSectionEvent sectionEvent = new MetaSectionEvent(wrapper);
		if (trigger != null)
			Variables.withLocalVariables(event, sectionEvent, () -> TriggerItem.walk(trigger, sectionEvent));

		return new MetaNode[] {sectionEvent.build()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<MetaNode> getReturnType() {
		return MetaNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("a new luckperms meta node")
				.append("with key", key, "and value", value)
				.toString();
	}

	public static class MetaSectionEvent extends Event {

		public MetaNodeWrapper node;

		public MetaNodeWrapper getNode() {
			return this.node;
		}

		public MetaNode build() {
			return node.build();
		}

		public MetaSectionEvent(MetaNodeWrapper node) {
			this.node = node;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}

