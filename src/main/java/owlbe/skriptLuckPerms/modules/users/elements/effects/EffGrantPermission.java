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
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

@Name("Grant Permission")
@Description("Adds a permission to a user.")
@Example("""
function example(p: offlineplayer, perm: string, duration: timespan=0 seconds):
	set {_lp} to luckperms user from {_p}
	if {_duration} is not 0 seconds:
		edit user {_lp}:
			grant permission {_perm} for {_duration}
		send "You just got %{_perm}% permission for %{_duration}%!" to {_p}
	else:
		edit user {_lp}:
			grant permission {_perm}
			send "You just got %{_perm}% permission!" to {_p}
		""")
@Since("1.0")
public class EffGrantPermission extends Effect {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffGrantPermission.class)
						.addPattern("(grant|add) [luckperm[s]] perm[ission] %luckpermspermission% [to %-luckpermsuser%] [for %-timespan%]")
						.build()
		);
	}

	private Expression<PermissionNode> permissionExpr;
	private Expression<User> userExpr;
	private Expression<Timespan> durationExpr;

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
		durationExpr = (Expression<Timespan>) expressions[2];
		return true;
	}

	@Override
	protected void execute(Event event) {
		PermissionNode permission = permissionExpr.getSingle(event);
		User user = userExpr != null ? userExpr.getSingle(event) : ((SecEditUser.UserEvent) event).getUser();
		if (permission == null || user == null)
			return;
		Timespan duration = durationExpr != null ? durationExpr.getSingle(event) : null;
		if (duration != null) {
			permission = PermissionNode.builder()
					.permission(permission.getKey())
					.context(permission.getContexts())
					.build();
			user.data().add(permission);
		} else {
			user.data().add(permission);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("grant permission", permissionExpr, "to", userExpr)
				.appendIf(durationExpr != null, "for", durationExpr)
				.toString();
	}

}
