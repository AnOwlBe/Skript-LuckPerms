package owlbe.skriptLuckPerms.modules.users.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

@Name("Permissions Of User")
@Description("""
		Represents the permissions of a user.
		See <a href='#EffGrantPermission'>grant permission</a> for how to add permissions to a user.
		""")
@Example("""
		function example(p: offlineplayer):
			set {_lp} to luckperms user from {_p}
			broadcast "%{_p}% has %size of luckperms permissions of {_lp}% permissions!%
			broadcast "their permissions: %luckperms permissions of {_lp}%"
		""")
@Since("1.0")
public class ExprPermissionsOfUser extends PropertyExpression<User, PermissionNode> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprPermissionsOfUser.class,
						PermissionNode.class,
						"luckperm[s] perm[ission]s",
						"luckpermsuser",
						false
				)
						.supplier(ExprPermissionsOfUser::new)
						.build()
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<User>) expressions[0]);
		return true;
	}

	@Override
	protected PermissionNode[] get(Event event, User[] users) {
		return Arrays.stream(users)
				.flatMap(user -> user.getNodes(NodeType.PERMISSION).stream())
				.toArray(PermissionNode[]::new);
	}

	@Override
	public Class<? extends PermissionNode> getReturnType() {
		return PermissionNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("luckperms permissions of")
				.toString();
	}

}
