package owlbe.skriptLuckPerms.modules.permholder.group.elements.effects;

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
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Load Group")
@Description("""
		Loads a LuckPerms group.
		
		The given group will be the most up-to-date version that LuckPerms has of it.
		""")
@Example("""
		set {_group} to luckperms group from "example"
		set {_perms::*} to luckperms permissions of {_group}
		# up to date!
		""")
@Since("INSERT VERSION")
public class EffLoadGroup extends AsyncEffect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffLoadGroup.class)
						.addPattern("set %-~objects% to luckperm[s] group [from] [name|key|id] %string%")
						.supplier(EffLoadGroup::new)
						.build()
		);
	}

	private Expression<String> groupName;
	private Expression<?> variable;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);

		groupName = (Expression<String>) expressions[1];
		variable = expressions[0];

		if (!ChangerUtils.acceptsChange(variable, ChangeMode.SET, User.class)) {
			Skript.error(variable.toString(null, Skript.debug()) + " cannot be set to a LuckPerms group.");
			return false;
		}

		return true;
	}

	@Override
	protected void execute(Event event) {
		String groupName = this.groupName.getSingle(event);
		if (groupName == null)
			return;

		Group group  = LuckPermsProvider.get().getGroupManager()
				.loadGroup(groupName)
				.join().orElse(null);

		variable.change(event, new Object[]{group}, ChangeMode.SET);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("set", variable)
				.append("to luckperms group from", groupName)
				.toString();
	}

}
