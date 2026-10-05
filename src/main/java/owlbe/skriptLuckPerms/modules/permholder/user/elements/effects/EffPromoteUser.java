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
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder;

@Name("Promote User")
@Description("""
	 Promotes a user along a track.
	 If the user is not on the track or is at end of track, nothing will happen.
	 """)
@Example("""
function example(p: offlineplayer,track: string):
	set {_lp} to luckperms user from {_p}
	edit user {_lp}:
		promote user {_lp} along track {_track}
	if {_p} is online:
		send "You were promoted on track %{_track}%!" to {_p}
		""")
@Since("1.0")
public class EffPromoteUser extends Effect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffPromoteUser.class)
						.addPattern("promote luckperm[s] user[s] %luckpermsusers% (along|on) [luckperm[s]] track[s] %luckpermstracks%")
						.build()
		);
	}

	private Expression<Track> tracks;
	private Expression<User> users;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!getParser().isCurrentEvent(SecEditHolder.HolderSectionEvent.class)) {
			Skript.error("You can only promote a user inside a 'edit permission holder' section");
			return false;
		}


		users = (Expression<User>) expressions[0];
		tracks  = (Expression<Track>) expressions[1];
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
				track.promote(user, ImmutableContextSet.empty());
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("promote luckperms users", users)
				.append("on tracks", tracks)
				.toString();
	}

}
