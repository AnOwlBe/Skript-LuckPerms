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

@Name("Group From Name")
@Description("Gets a LuckPerms group by the provided name if it exists.")
@Example("""
		set prefix of group (group "example") to {_chatmeta}
		""")
@Since("INSERT VERSION")
public class ExprGroupFromName extends SimpleExpression<Group> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprGroupFromName.class, Group.class)
						.addPatterns("[the] luckperm[s] group [from] %string%")
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
	protected Group[] get(Event event) {
		String name = this.name.getSingle(event);
		if (name == null)
			return new Group[0];

		Group group;
		group = LuckPermsProvider.get().getGroupManager().getGroup(name);

		if (group == null)
			return new Group[0];
		return new Group[]{group};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Group> getReturnType() {
		return Group.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "luckperms group from" + name.toString(event, debug);
	}

}
