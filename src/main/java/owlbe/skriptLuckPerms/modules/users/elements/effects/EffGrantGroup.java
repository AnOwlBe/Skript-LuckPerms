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
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

import static owlbe.skriptLuckPerms.modules.users.UserUtils.getUser;

@Name("Grant Group")
@Description("Adds a group to a user.")
@Example("""
function example(p: offlineplayer,group: string,duration: timespan=0 seconds):
	set {_lp} to luckperms user from {_p}
	if {_duration} is not 0 seconds:
		edit user {_lp}:
			grant luckperms group {_group} for {_duration}
		send "You just got %{_group}% group for %{_duration}%!" to {_p}
	else:
		edit user {_lp}:
			grant luckperms group {_group}
			send "You just got %{_group}% permission!" to {_p}
		""")
@Since("1.0")
public class EffGrantGroup extends Effect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffGrantGroup.class)
						.addPattern("(grant|add) luckperm[s] group %luckpermsgroup% [to %-luckpermsuser%] [for %-timespan%]")
						.build()
		);
	}

	private Expression<Group> group;
	private Expression<User> user;
	private Expression<Timespan> duration;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(SecEditUser.UserEvent.class)) {
			Skript.error("This can only be used inside an 'edit user' section");
			return false;
		}

		group = (Expression<Group>) expressions[0];
		if (expressions[1] != null)
			user = (Expression<User>) expressions[1];
		duration = (Expression<Timespan>) expressions[2];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Group group = this.group.getSingle(event);
		User user = getUser(event, this.user);
		if (group == null || user == null)
			return;
		Timespan duration = this.duration != null ? this.duration.getSingle(event) : null;

		if (duration == null) {
			user.data().add(InheritanceNode.builder(group).build());
			return;
		}
		user.data().add(InheritanceNode.builder(group)
				.expiry(duration.getDuration())
				.build());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("grant group", group, "to", user)
				.appendIf(duration != null, "for", duration)
				.toString();
	}

}
