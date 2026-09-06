package owlbe.skriptLuckPerms.modules.test.elements.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.user.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.test.TestModule;

public class ExprTestUser extends SimpleExpression<User> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprTestUser.class, User.class)
						.addPattern("test-luck[ ]perms user")
						.build()
		);
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		return true;
	}

	@Override
	protected User[] get(Event event) {
		return new User[]{TestModule.getTestUser()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends User> getReturnType() {
		return User.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "test-luckperms user";
	}

}
