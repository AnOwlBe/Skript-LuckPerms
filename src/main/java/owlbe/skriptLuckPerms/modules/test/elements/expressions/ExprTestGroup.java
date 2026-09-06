package owlbe.skriptLuckPerms.modules.test.elements.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.test.TestModule;

public class ExprTestGroup extends SimpleExpression<Group> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprTestGroup.class, Group.class)
						.addPattern("test-luck[ ]perms group")
						.build()
		);
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		return true;
	}

	@Override
	protected Group[] get(Event event) {
		return new Group[]{TestModule.getTestGroup()};
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
		return "test-luckperms group";
	}

}
