package owlbe.skriptLuckPerms.modules.users.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

@Name("Revoke Permission")
@Description("Removes a permission from a user.")
@Example("""
function example(p: offlineplayer,perm: string):
	set {_lp} to luckperms user from {_p}
	edit user {_lp}:
		revoke permission {_perm}
	send "You just lost permission %{_perm}% ;c" to {_p}
		""")
@Since("1.0")
public class EffRevokePermission extends Effect {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffRevokePermission.class)
						.addPattern("(revoke|remove) [luckperm[s]] perm[ission] %luckpermspermission% [from %-luckpermsuser%]")
						.build()
		);
	}

	private Expression<PermissionNode> permissionExpr;
	private Expression<User> userExpr;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(SecEditUser.UserEvent.class)) {
			Skript.error("This can only be used inside an 'edit user' section");
			return false;
		}
		permissionExpr = (Expression<PermissionNode>) expressions[0];
		if (expressions[1] != null)
			userExpr = (Expression<User>) expressions[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		PermissionNode permission = permissionExpr.getSingle(event);
		User user = userExpr != null ? userExpr.getSingle(event) : ((SecEditUser.UserEvent) event).getUser();
		if (permission == null || user == null)
			return;
		user.data().remove(permission);
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return new SyntaxStringBuilder(event, b)
				.append("revoke permission", permissionExpr, "from", userExpr)
				.toString();
	}

}
