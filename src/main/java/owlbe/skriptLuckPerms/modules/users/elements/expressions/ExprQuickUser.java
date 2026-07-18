package owlbe.skriptLuckPerms.modules.users.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Quick User")
@Description("""
		Gets a LuckPerms user from an offline player.
		This is used for cases when you need instant returns and the user is online.
		For offline players or online & offline players support use `EffLoadPlayer`
		This may not return the most up to date version of the user's LuckPerms data.
		""")
@Example("""
		function example(p: offlineplayer):
			set {_m} to quick luckperms user from {_p}
			broadcast "%{_p}% has %size of groups of {_lp}% groups!"
		""")
@Since("1.0.2")
public class ExprQuickUser extends SimpleExpression<User> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprQuickUser.class, User.class)
						.addPattern("quick luckperm[s] user [from] %player%")
						.build()
		);
	}

	private Expression<Player> player;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		player = (Expression<Player>) expressions[0];
		return true;
	}

	@Override
	protected User[] get(Event event) {
		Player player = this.player.getSingle(event);
		if (player == null)
			return new User[0];

		User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
		return new User[]{user};

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
		return new SyntaxStringBuilder(event, debug)
				.append("quick luckperms user from", player)
				.toString();
	}

}
