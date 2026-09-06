package owlbe.skriptLuckPerms.modules.permholder.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Has LuckPerms Permission")
@Description(""" 
		 Checks whether a permission holder (a user or group) has the given LuckPerms permission.
		 """)
@Example("""
		function hasPerm(p: player, permission: string) :: boolean:
			set {_lp} to luckperms user from {_p}
			if {_lp} has luckperms permission {_permission}:
				return true
			else:
				return false
		""")
@Since("1.0")
public class CondHasPermission extends Condition {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.CONDITION,
				PropertyCondition.infoBuilder(
								CondHasPermission.class, PropertyCondition.PropertyType.HAVE,
								"luckperm[s] perm[ission][s] %luckpermspermissions%", "luckpermspermissionholders")
						.supplier(CondHasPermission::new)
						.build());
	}

	private Expression<PermissionHolder> holders;
	private Expression<PermissionNode> permissions;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		holders = (Expression<PermissionHolder>) expressions[0];
		permissions = (Expression<PermissionNode>) expressions[1];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		return this.holders.check(event, holder -> {
			return SimpleExpression.check(permissions.getArray(event), permission -> holder.getNodes(NodeType.PERMISSION).contains(permission), false, permissions.getAnd());
		}, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return PropertyCondition.toString(this, PropertyCondition.PropertyType.HAVE, event, debug, holders, "luckperms permission " + permissions.toString(event, debug));
	}

}
