package owlbe.skriptLuckPerms.modules.track.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Track From Name")
@Description("Gets a LuckPerms track by the provided name if it exists.")
@Example("""
		set {_track} to luckperms track from "staff"
		edit luckperms track {_track}:
		    add {_mygroup} to luckperms groups of event-track
		""")
@Since("INSERT VERSION")
public class ExprTrackFromName extends SimpleExpression<Track> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprTrackFromName.class, Track.class)
						.addPatterns("[the] luckperm[s] track [from|named|with name] %string%")
						.supplier(ExprTrackFromName::new)
						.build()
		);
	}

	private Expression<String> name;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		name = (Expression<String>) expressions[0];
		return true;
	}

	@Override
	protected Track[] get(Event event) {
		String name = this.name.getSingle(event);
		if (name == null)
			return new Track[0];

		Track track;
		track = LuckPermsProvider.get().getTrackManager().getTrack(name);

		if (track == null)
			return new Track[0];

		return new Track[] {track};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Track> getReturnType() {
		return Track.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "luckperms track from " + name.toString(event, debug);
	}

}
