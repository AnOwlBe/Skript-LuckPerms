package owlbe.skriptLuckPerms.modules.permholder.user.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.ArrayList;
import java.util.List;

@Name("Player From User")
@Description("""
		Returns offline player(s) from the provided LuckPerms user(s).
		""")
@Example("""
		function example(user: luckpermsuser):
			broadcast player from user {_user}
		""")
@Since("1.0")
public class ExprPlayerFromUser extends SimpleExpression<OfflinePlayer> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprPlayerFromUser.class, OfflinePlayer.class)
						.addPattern("[offline[ ]]player[s] from luckperm[s] user %luckpermsusers%")
						.build()
		);
	}

	private Expression<User> users;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		users = (Expression<User>) expressions[0];
		return true;
	}

	@Override
	protected OfflinePlayer[] get(Event event) {
	   User[] users = this.users.getArray(event);
	   if (users == null)
		   return new OfflinePlayer[0];

		List<OfflinePlayer> players = new ArrayList<>();

		for (User user : users)
			players.add(Bukkit.getOfflinePlayer(user.getUniqueId()));

	   return players.toArray(OfflinePlayer[]::new);

	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends OfflinePlayer> getReturnType() {
		return OfflinePlayer.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "player from users " + users.toString(event, debug);
	}

}
