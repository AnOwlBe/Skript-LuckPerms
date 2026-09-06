package owlbe.skriptLuckPerms.modules.permholder.group.elements.effects;

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
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Delete Group")
@Description("""
		Deletes the given LuckPerms group.
		""")
@Example("""
		function example(name: string):
			delete luckperms group named {_name}
		""")
@Since("1.0.2")
public class EffDeleteGroup extends AsyncEffect {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffDeleteGroup.class)
						.addPattern("delete luckperm[s] group %luckpermsgroup%")
						.build()
		);
	}

	private Expression<Group> group;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);
		group = (Expression<Group>) expressions[0];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Group group = this.group.getSingle(event);
		if (group == null)
			return;
		LuckPermsProvider.get().getGroupManager().deleteGroup(group);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("delete luckperms group", group)
				.toString();
	}

}
