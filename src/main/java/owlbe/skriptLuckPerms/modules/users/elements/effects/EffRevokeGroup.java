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
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

@Name("Revoke Group")
@Description("Removes a group from a user.")
@Example("""
function example(p: offlineplayer,group: string):
	set {_lp} to luckperms user from {_p}
	edit user {_lp}:
		revoke luckperms group {_group}
	send "You just lost group %{_group}% ;c" to {_p}
		""")
@Since("1.0")
public class EffRevokeGroup extends Effect {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffRevokeGroup.class)
						.addPatterns("(revoke|remove) luckperm[s] group [%-luckpermsgroup%] [from %-luckpermsuser%]")
						.build()
		);
	}

	private Expression<Group> groupExpr;
	private Expression<User> userExpr;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(SecEditUser.UserEvent.class)) {
			Skript.error("This can only be used inside an 'edit user' section");
			return false;
		}
		groupExpr = (Expression<Group>) expressions[0];
		if (expressions[1] != null)
			userExpr = (Expression<User>) expressions[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Group group = groupExpr.getSingle(event);
		User user = userExpr != null ? userExpr.getSingle(event) : ((SecEditUser.UserEvent) event).getUser();
		if (group == null || user == null)
			return;
		if (user.getNodes(NodeType.INHERITANCE).stream()
				.map(InheritanceNode::getGroupName)
				.noneMatch(lpGroup-> lpGroup.equals(group.getName())))
			return;
		user.data().remove(InheritanceNode.builder(group).build());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("revoke group", groupExpr, "from", userExpr)
				.toString();
	}

}
