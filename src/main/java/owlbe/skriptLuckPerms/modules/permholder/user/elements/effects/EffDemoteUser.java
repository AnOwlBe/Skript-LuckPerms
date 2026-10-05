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

@Name("Demote User")
@Description("""
	 Demotes the provided user(s) along the provided track(s).
	 If provided user(s) aren't not on the provided track(s) nothing will happen.
	 """)
@Example("""
function example(p: offlineplayer,track: string):
	set {_lp} to luckperms user from {_p}
	edit user {_lp}:
		demote user {_lp} along track {_track}
	if {_p} is online:
		send "You were demoted on track %{_track}%!" to {_p}
		""")
@Since("INSERT VERSION")
public class EffDemoteUser extends Effect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffDemoteUser.class)
						.addPattern("demote luckperm[s] user[s] %luckpermsusers% (along|on) [luckperm[s]] track[s] %luckpermstracks%")
						.build()
		);
	}

	private Expression<Track> tracks;
	private Expression<User> users;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(HolderSectionEvent.class)) {
			Skript.error("You can only demote a user inside a 'edit permission holder' section");
			return false;
		}

		users = (Expression<User>) expressions[0];
		tracks = (Expression<Track>) expressions[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Track[] tracks = this.tracks.getArray(event);
		User[] users = this.users.getArray(event);
		if (tracks == null || users == null)
			return;

		for (User user : users) {
			for (Track track : tracks)
				track.demote(user, ImmutableContextSet.empty());
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("demote luckperms users ", users, "on luckperms tracks", tracks)
				.toString();
	}

}
