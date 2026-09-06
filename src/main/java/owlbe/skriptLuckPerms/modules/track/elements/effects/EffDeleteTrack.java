package owlbe.skriptLuckPerms.modules.track.elements.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Delete Track")
@Description("Deletes a luckperms track.")
@Example("""
		function example(name: string):
			delete luckperms track named {_name}
		""")
@Since("1.0.2")
@SuppressWarnings("unchecked")
public class EffDeleteTrack extends AsyncEffect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffDeleteTrack.class)
						.addPattern("delete luckperm[s] track %luckpermstrack%")
						.build()
		);
	}

	private Expression<Track> track;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);
		track = (Expression<Track>) expressions[0];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Track track = this.track.getSingle(event);
		if (track == null)
			return;

		LuckPermsProvider.get().getTrackManager().deleteTrack(track);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "delete luckperms track" + track.toString(event, debug);
	}

}

