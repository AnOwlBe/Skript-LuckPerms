package owlbe.skriptLuckPerms.modules.users.elements.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
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

@Name("Has LuckPerms Permission")
@Description(""" 
		 Returns whether a user has/doesn't have a permission.
		 This is different from Skript's has permission condition as with `user` this supports offline players.
		 """)
@Example("""
		function hasPerm(p: player,perm: string) :: boolean:
			set {_lp} to luckperms user from {_p}
			if {_lp} has luckperms permission {_perm}:
				return true
			else:
				return false
		""")
@Since("1.0, INSERT VERSION (luckpermspermission)")
public class CondHasPermission extends Condition {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.CONDITION,
				SyntaxInfo.builder(CondHasPermission.class)
						.addPatterns(
								"%luckpermsuser% has luckperm[s] perm[ission] %luckpermspermission%",
								"%luckpermsuser% (doesn't|does not) have luckperm[s] perm[ission] %luckpermspermission%"
						)
						.build()
		);
	}

	private Expression<User> user;
	private Expression<PermissionNode> perm;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		user = (Expression<User>) expressions[0];
		perm = (Expression<PermissionNode>) expressions[1];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		User user = this.user.getSingle(event);
		if (user == null)
			return false;
		PermissionNode perm = this.perm.getSingle(event);
		if (perm == null)
			return false;
		return isNegated() != user.getNodes().contains(perm);
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return new SyntaxStringBuilder(event, b)
				.append(user, isNegated() ? "doesn't have permission" : "has permission", perm)
				.toString();
	}

}
