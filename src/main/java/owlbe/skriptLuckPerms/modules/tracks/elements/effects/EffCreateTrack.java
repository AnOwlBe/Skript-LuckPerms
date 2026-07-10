package owlbe.skriptLuckPerms.modules.tracks.elements.effects;

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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Create Track")
@Description("Creates a new luckperms track and then loads it into memory.")
@Example("""
		function example(name: string):
			create new luckperms track named {_name}
		""")
@Since("1.0.2")
@SuppressWarnings("unchecked")
public class EffCreateTrack extends AsyncEffect {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffCreateTrack.class)
						.addPattern("create [new] luckperm[s] track (with name|named) %string%")
						.build()
		);
	}

	private Expression<String> nameExpr;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);
		nameExpr = (Expression<String>) expressions[0];
		return true;
	}

	@Override
	protected void execute(Event event) {
		String name = nameExpr.getSingle(event);
		if (name == null)
			return;
		// No public method to check if name is valid for some reason but also names don't support spaces.
		if (!name.matches("[a-z0-9_.-]+"))
			return;
		LuckPermsProvider.get().getTrackManager().createAndLoadTrack(name);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("create luckperms track", nameExpr)
				.toString();
	}

}

