package owlbe.skriptLuckPerms.modules.permholder.user.elements.effects;

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
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;

import static owlbe.skriptLuckPerms.modules.permholder.user.UserUtils.getUser;

@Name("Demote User")
@Description("""
	 Demotes a user along a track.
	 If the user is not on the track nothing will happen.
	 """)
@Example("""
function example(p: offlineplayer,track: string):
	set {_lp} to luckperms user from {_p}
	edit user {_lp}:
		demote user {_lp} along track {_track}
	if {_p} is online:
		send "You were demoted on track %{_track}%!" to {_p}
		""")
@Since({"1.0", "INSERT VERSION (demote luckperms user)"})
public class EffDemoteUser extends Effect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffDemoteUser.class)
						.addPattern("demote luckperm[s] user [%-luckpermsuser%] (along|on) [luckperm[s]] track %luckpermstrack%")
						.build()
		);
	}

	private Expression<Track> track;
	private Expression<User> user;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(HolderSectionEvent.class)) {
			Skript.error("You can only demote a user inside a 'edit permission holder' section");
			return false;
		}

		if (expressions[0] != null)
			user = (Expression<User>) expressions[0];
		track = (Expression<Track>) expressions[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Track track = this.track.getSingle(event);
		User user = getUser(event, this.user);
		if (track == null || user == null)
			return;

		track.demote(user, ImmutableContextSet.empty());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("demote luckperms ", user, "on luckperms track", track)
				.toString();
	}

}
