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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Create Track")
@Description("""
        Creates a new LuckPerms track and then loads it into memory.
        
        If a track by the same name already exists, it will be loaded.
        """)
@Example("""
		function example(name: string):
			create new luckperms track named {_name}
		""")
@Since({"1.0.2", "INSERT VERSION (optional 'a' in pattern"})
public class EffCreateTrack extends AsyncEffect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffCreateTrack.class)
						.addPattern("create [a] [new] luckperm[s] track (with name|named) %string%")
						.build()
		);
	}

	private Expression<String> name;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);
		name = (Expression<String>) expressions[0];
		return true;
	}

	@Override
	protected void execute(Event event) {
		String name = this.name.getSingle(event);
		if (name == null)
			return;

		if (name.contains(" ") || name.length() >= 36) {
			error("A track's name cannot have spaces or be above 36 characters!");
			return;
			// TODO: Test this and ensure it functions
			// also unsure if the limit really is 36 characters but we'll see
		}

		LuckPermsProvider.get().getTrackManager().createAndLoadTrack(name);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "create luckperms track named" + name.toString(event, debug);
	}

}

