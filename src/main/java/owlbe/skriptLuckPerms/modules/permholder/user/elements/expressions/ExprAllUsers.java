package owlbe.skriptLuckPerms.modules.permholder.user.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("All Loaded Users")
@Description("Returns a list of all LuckPerms users that are currently loaded.")
@Example("""
		set {_users::*} to all loaded luckperms users
		loop {_users::*}:
		    edit user loop-value:
		        grant luckperms group "admin" to event-user for 30 seconds
		        set {_p} to player from luckperms user event-user
		        send "You now have admin for 30s!" to {_p}
		""")
@Since("INSERT VERSION")
public class ExprAllUsers extends SimpleExpression<User> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprAllUsers.class, User.class)
						.addPatterns("all [of the] [loaded] luckperm[s] users")
						.build()
		);
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		return true;
	}

	@Override
	protected User[] get(Event event) {
		return LuckPermsProvider.get().getUserManager().getLoadedUsers().toArray(new User[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends User> getReturnType() {
		return User.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "all loaded luckperms users";
	}

}
