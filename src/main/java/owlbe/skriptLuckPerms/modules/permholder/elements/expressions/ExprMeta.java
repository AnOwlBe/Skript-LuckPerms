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
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;

import java.util.Arrays;

@Name("Custom Meta")
@Description("""
		    The custom meta of a permission holder (a user or group).
		    Can be set, added to, cleared or removed.
		    """)
//TODO this example
@Example("""
		function
		""")
@Since("INSERT VERSION")
public class ExprMeta extends PropertyExpression<PermissionHolder, MetaNode> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprMeta.class,
						MetaNode.class,
						"[custom] luckperm[s] meta",
						"luckpermspermissionholder",
						false
				)
						.supplier(ExprMeta::new)
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
	protected MetaNode[] get(Event event, PermissionHolder[] holders) {
		return Arrays.stream(holders)
				.flatMap(holder -> holder.getNodes(NodeType.META).stream())
				.toArray(MetaNode[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (!getParser().isCurrentEvent(HolderSectionEvent.class)) {
			Skript.error("You can only change the meta of a holder inside a 'edit permission holder' section");
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

		MetaNode meta = delta != null ? (MetaNode) delta[0] : null;

		if (mode != ChangeMode.RESET && meta == null)
			return;

		switch (mode) {
			case SET -> {
				holder.data().clear(NodeType.META::matches);
				holder.data().add(meta);
			}
			case ADD -> holder.data().add(meta);
			case RESET -> holder.data().clear(NodeType.META::matches);
			case REMOVE -> holder.data().remove(meta);
		}
	}

	@Override
	public Class<? extends MetaNode> getReturnType() {
		return MetaNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "custom luckperms meta of" + getExpr().toString(event, debug);
	}

}
