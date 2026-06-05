package owlbe.skriptLuckPerms.modules.users.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@SuppressWarnings("unchecked")
@Name("Quick Load Player")
@Description("""
        Quickly gets a luckperms user from memory.
        This is used for cases when you need instant returns and the user is online.
        For offline players or online & offline players support use `EffLoadPlayer`
        This may not return the most up to date version of the user's LuckPerms data.
        """)
@Example("""
        function example(p: offlineplayer):
            set {_m} to quick luckperms user {_p}
            broadcast "%{_p}% has %size of groups of {_lp}% groups!"
        """)
@Since("1.1.3")
public class EffQuickLoadPlayer extends Effect {
	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EFFECT,
			SyntaxInfo.builder(EffQuickLoadPlayer.class)
						.addPatterns("set %-~objects% to quick luckperm[s] user [from] %player%")
						.build()

		);
	}

	private Expression<Player> playerExpr;
	private Expression<?> varExpr;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		playerExpr = (Expression<Player>) expressions[1];
		varExpr = expressions[0];
		if (!Changer.ChangerUtils.acceptsChange(varExpr, Changer.ChangeMode.SET, User.class)) {
			Skript.error(varExpr.toString(null, Skript.debug()) + " cannot be set to a LuckPerms user.");
			return false;
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		Player player = playerExpr.getSingle(event);
		if (player == null) return;
		User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
		varExpr.change(event, new Object[]{user}, Changer.ChangeMode.SET);
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return new SyntaxStringBuilder(event, b)
				.append("set")
				.append(varExpr)
				.append("to quick luckperms user from")
				.append(playerExpr)
				.toString();
	}
}
