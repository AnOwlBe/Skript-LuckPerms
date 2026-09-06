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
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;

import java.util.Arrays;

@Name("Permissions")
@Description("""
		    The permissions of a permission holder (a user or group).
		    Can be set, added to, cleared or removed.
		    """)
@Example("""
		function example(p: offlineplayer):
			set {_lp} to luckperms user from {_p}
			broadcast "%{_p}% has %size of luckperms permissions of {_lp}% permissions!%
			broadcast "their permissions: %luckperms permissions of {_lp}%"
		""")
@Since("1.0, INSERT VERSION (supports groups)")
public class ExprPermissions extends PropertyExpression<PermissionHolder, PermissionNode> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprPermissions.class,
						PermissionNode.class,
						"luckperm[s] perm[ission]s",
						"luckpermspermissionholder",
						false
				)
						.supplier(ExprPermissions::new)
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
	protected PermissionNode[] get(Event event, PermissionHolder[] holders) {
		return Arrays.stream(holders)
				.flatMap(holder -> holder.getNodes(NodeType.PERMISSION).stream())
				.toArray(PermissionNode[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (!getParser().isCurrentEvent(HolderSectionEvent.class)) {
			Skript.error("You can only change the permissions of a holder inside an 'edit permission holder' section");
			return null;
		}

		return switch (mode) {
			case SET, ADD, RESET, REMOVE -> CollectionUtils.array(PermissionNode.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		PermissionHolder holder = getExpr().getSingle(event);
		if (holder == null)
			return;

		PermissionNode permission = delta != null ? (PermissionNode) delta[0] : null;

		if (mode != ChangeMode.RESET && permission == null)
			return;

		switch (mode) {
			case SET -> {
				holder.data().clear(NodeType.PERMISSION::matches);
				holder.data().add(permission);
			}
			case ADD -> holder.data().add(permission);
			case RESET -> holder.data().clear(NodeType.PERMISSION::matches);
			case REMOVE -> holder.data().remove(permission);
		}
	}


	@Override
	public Class<? extends PermissionNode> getReturnType() {
		return PermissionNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "luckperms permissions of" + getExpr().toString(event, debug);
	}

}
