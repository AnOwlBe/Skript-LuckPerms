package owlbe.skriptLuckPerms.modules.permholder.group.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("All Groups")
@Description("Returns a list of all LuckPerms groups.")
@Example("""
		command /getallgroups:
			trigger:
				 send all of the luckperms groups to player
		""")
@Since("1.0, INSERT VERSION (returns groups)")
public class ExprAllGroups extends SimpleExpression<Group> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprAllGroups.class, Group.class)
						.addPatterns("all [of the] luckperm[s] groups")
						.build()
		);
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		return true;
	}

	@Override
	protected Group[] get(Event event) {
		return LuckPermsProvider.get().getGroupManager().getLoadedGroups().toArray(Group[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Group> getReturnType() {
		return Group.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "all luckperms groups";
	}

}
