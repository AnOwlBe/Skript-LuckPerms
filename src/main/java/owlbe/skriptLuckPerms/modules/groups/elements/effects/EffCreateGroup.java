package owlbe.skriptLuckPerms.modules.groups.elements.effects;

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

import static owlbe.skriptLuckPerms.utilitities.LuckPermsUtils.isValidNodeName;

@Name("Create Group")
@Description("""
		Creates a new LuckPerms group with the given name and then loads it into memory.
		""")
@Example("""
		function example(name: string):
			create new luckperms group named {_name}
		""")
@Since("1.0.2")
public class EffCreateGroup extends AsyncEffect {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffCreateGroup.class)
						.addPattern("create [new] luckperm[s] group (with name|named) %string%")
						.build()
		);
	}

	private Expression<String> name;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);
		name = (Expression<String>) expressions[0];
		return true;
	}

	@Override
	protected void execute(Event event) {
		String name = this.name.getSingle(event);
		if (name == null)
			return;

		if (!isValidNodeName(name))
			return;
		LuckPermsProvider.get().getGroupManager().createAndLoadGroup(name);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("create luckperms group", name)
				.toString();
	}

}
