package owlbe.skriptLuckPerms.modules.track.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.Changer.ChangerUtils;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Load Track")
@Description("""
		Loads a LuckPerms track.
		
		The given track will be the most up-to-date version that LuckPerms has of it.
		""")
@Example("""
		
		""")
@Since("INSERT VERSION")
public class EffLoadTrack extends AsyncEffect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffLoadTrack.class)
						.addPattern("set %-~objects% to luckperm[s] track [from] [name|key|id] %string%")
						.supplier(EffLoadTrack::new)
						.build()
		);
	}

	private Expression<String> trackName;
	private Expression<?> variable;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);

		trackName = (Expression<String>) expressions[1];
		variable = expressions[0];

		if (!ChangerUtils.acceptsChange(variable, ChangeMode.SET, User.class)) {
			Skript.error(variable.toString(null, Skript.debug()) + " cannot be set to a LuckPerms group.");
			return false;
		}

		return true;
	}

	@Override
	protected void execute(Event event) {
		String trackName = this.trackName.getSingle(event);
		if (trackName == null)
			return;

		Track track  = LuckPermsProvider.get().getTrackManager()
				.loadTrack(trackName)
				.join().orElse(null);

		variable.change(event, new Object[]{track}, ChangeMode.SET);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("set", variable)
				.append("to luckperms track from", trackName)
				.toString();
	}

}
