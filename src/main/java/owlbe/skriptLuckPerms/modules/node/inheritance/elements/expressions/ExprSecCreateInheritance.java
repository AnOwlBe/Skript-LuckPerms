package owlbe.skriptLuckPerms.modules.node.inheritance.elements.expressions;

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
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.utils.wrapper.InheritanceNodeWrapper;

import java.util.List;

@Name("Inheritance")
@Description("""
		Creates a inheritance with the given values.
		
		See <the ref> for how to add a inheritance node to a permission holder.
		""")
@Example("""
        set {_inheritance} to a new inheritance node from group "example":
            set expiry of event-inheritance node to 24 hours
            set context of event-inheritance node to {_mycontext}
        """)
@Since("INSERT VERSION")
public class ExprSecCreateInheritance extends SectionExpression<InheritanceNode> {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprSecCreateInheritance.class, InheritanceNode.class)
						.addPatterns("[a] [new] luckperm[s] inheritance [node] (with|from) group %luckpermsgroup%")
						.supplier(ExprSecCreateInheritance::new)
						.build()
		);

		eventValueRegistry.register(EventValue.builder(InheritanceSectionEvent.class, InheritanceNodeWrapper.class)
				.getter(InheritanceSectionEvent::getNode)
				.patterns("inheritance [node]")
				.build());
	}

	private Expression<Group> group;
	private @Nullable Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItem) {
		group = (Expression<Group>) expressions[0];

		if (sectionNode != null) {
			trigger = SectionUtils.loadLinkedCode("inheritance node section", (beforeLoading, afterLoading)
					-> loadCode(sectionNode, "inheritance node section", beforeLoading, afterLoading, InheritanceSectionEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected InheritanceNode @Nullable [] get(Event event) {
		Group group = this.group.getSingle(event);
		if (group == null)
			return new InheritanceNode[0];

		InheritanceNodeWrapper wrapper = new InheritanceNodeWrapper(group);

		InheritanceSectionEvent sectionEvent = new InheritanceSectionEvent(wrapper);
		if (trigger != null)
			Variables.withLocalVariables(event, sectionEvent, () -> TriggerItem.walk(trigger, sectionEvent));

		return new InheritanceNode[] {sectionEvent.build()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<InheritanceNode> getReturnType() {
		return InheritanceNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("a new luckperms inheritance node")
				.append("from group", group)
				.toString();
	}

	public static class InheritanceSectionEvent extends Event {

		public InheritanceNodeWrapper node;

		public InheritanceNodeWrapper getNode() {
			return this.node;
		}

		public InheritanceNode build() {
			return node.build();
		}

		public InheritanceSectionEvent(InheritanceNodeWrapper node) {
			this.node = node;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}
