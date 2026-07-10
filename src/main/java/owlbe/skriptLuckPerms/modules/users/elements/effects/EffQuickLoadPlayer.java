package owlbe.skriptLuckPerms.modules.users.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.Changer.ChangerUtils;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Quick Load Player")
@Description("""
		Quickly gets a luckperms user from memory.
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
public class EffQuickLoadPlayer extends Effect {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EFFECT,
			SyntaxInfo.builder(EffQuickLoadPlayer.class)
						.addPattern("set %-~objects% to quick luckperm[s] user [from] %player%")
						.build()

		);
	}

	private Expression<Player> playerExpr;
	private Expression<?> varExpr;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		playerExpr = (Expression<Player>) expressions[1];
		varExpr = expressions[0];
		if (!ChangerUtils.acceptsChange(varExpr, ChangeMode.SET, User.class)) {
			Skript.error(varExpr.toString(null, Skript.debug()) + " cannot be set to a LuckPerms user.");
			return false;
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		Player player = playerExpr.getSingle(event);
		if (player == null)
			return;
		User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
		varExpr.change(event, new Object[]{user}, ChangeMode.SET);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("set", varExpr)
				.append("to quick luckperms user from", playerExpr)
				.toString();
	}

}
