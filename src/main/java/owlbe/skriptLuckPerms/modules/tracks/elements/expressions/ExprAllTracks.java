package owlbe.skriptLuckPerms.modules.tracks.elements.expressions;

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

@Name("All Tracks")
@Description("returns a list of all LuckPerms tracks.")
@Example("""
		command /getalltracks:
			trigger:
				 send all of the luckperms tracks to player
		""")
@Since("1.0")
public class ExprAllTracks extends SimpleExpression<String> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprAllTracks.class, String.class)
						.addPattern("all [of the] luckperm[s] tracks")
						.build()
		);
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		return true;
	}

	@Override
	protected String[] get(Event event) {
		return LuckPermsProvider.get().getTrackManager().getLoadedTracks().stream()
				.map(Track::getName)
				.toArray(String[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "all tracks";
	}

}
