package owlbe.skriptLuckPerms.modules.permholder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;

import java.util.Arrays;

@Name("Inheritances")
@Description("""
		The groups a permission holder (a user or group) belongs to, along with extra information
		about that membership, such as its expiry or context.
		
		This does not contain information about the group itself (such as its weight or display name).
		To get that, use the groups of expression.
		""")
//TODO this example
@Example("""
		function
		""")
@Since("INSERT VERSION")
public class ExprInheritances extends PropertyExpression<PermissionHolder, InheritanceNode> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprInheritances.class,
						InheritanceNode.class,
						"luckperm[s] inheritances",
						"luckpermspermissionholder",
						false
				)
						.supplier(ExprInheritances::new)
						.build()
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<PermissionHolder>) expressions[0]);
		return true;
	}

	@Override
	protected InheritanceNode[] get(Event event, PermissionHolder[] holders) {
		return Arrays.stream(holders)
				.flatMap(holder -> holder.getNodes(NodeType.INHERITANCE).stream())
				.toArray(InheritanceNode[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (!getParser().isCurrentEvent(HolderSectionEvent.class)) {
			Skript.error("You can only change the inheritances of a holder inside a 'edit permission holder' section");
			return null;
		}

		return switch (mode) {
			case SET, ADD, RESET, REMOVE -> CollectionUtils.array(MetaNode.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		PermissionHolder holder = getExpr().getSingle(event);
		if (holder == null)
			return;

		InheritanceNode inheritance = delta != null ? (InheritanceNode) delta[0] : null;

		if (mode != ChangeMode.RESET && inheritance == null)
			return;

		switch (mode) {
			case SET -> {
				holder.data().clear(NodeType.INHERITANCE::matches);
				holder.data().add(inheritance);
			}
			case ADD -> holder.data().add(inheritance);
			case RESET -> holder.data().clear(NodeType.INHERITANCE::matches);
			case REMOVE -> holder.data().remove(inheritance);
		}
	}

	@Override
	public Class<? extends InheritanceNode> getReturnType() {
		return InheritanceNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "luckperms inheritances of" + getExpr().toString(event, debug);
	}

}
