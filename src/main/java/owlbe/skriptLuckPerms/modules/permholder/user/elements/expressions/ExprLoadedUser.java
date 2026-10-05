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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.ArrayList;
import java.util.List;

@Name("Loaded User")
@Description("""
		Gets a LuckPerms user from an online player.
		This is used for cases when you need instant returns and the user is online.
		
		For offline players or online and offline players support use `EffLoadPlayer`
		This may not return the most up to date version of the user's LuckPerms data.
		""")
@Example("""
		function example(p: offlineplayer):
			set {_m} to quick luckperms user from {_p}
			broadcast "%{_p}% has %size of groups of {_lp}% groups!"
		""")
@Since({"1.0.2", "INSERT VERSION ('loaded luckperms user from player')"})
public class ExprLoadedUser extends SimpleExpression<User> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprLoadedUser.class, User.class)
						.addPattern("loaded luckperm[s] user [from [player[s]] ] %players%")
						.build()
		);
	}

	private Expression<Player> players;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		players = (Expression<Player>) expressions[0];
		return true;
	}

	@Override
	protected User[] get(Event event) {
		Player[] players = this.players.getArray(event);
		if (players == null)
			return new User[0];

		List<User> users = new ArrayList<>();

		for (Player player : players) {
			User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
			users.add(user);
		}

		return users.toArray(User[]::new);

	}

	@Override
	public boolean isSingle() {
		return players.isSingle();
	}

	@Override
	public Class<? extends User> getReturnType() {
		return User.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "quick luckperms user from players " + players.toString(event, debug);
	}

}
