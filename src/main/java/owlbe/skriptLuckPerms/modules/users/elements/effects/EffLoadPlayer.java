package owlbe.skriptLuckPerms.modules.users.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.Changer.ChangerUtils;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Load Player")
@Description("""
		Loads a LuckPerms user.
		This is required for almost everything relating to users.
		""")
@Example("""
		function example(p: offlineplayer):
			set {_lp} to luckperms user from {_p}
			broadcast "%{_p}% has %size of groups of {_lp}% groups!"
		""")
@Since("1.0, 1.0.2 (pattern change), INSERT VERSION (player)")
public class EffLoadPlayer extends AsyncEffect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffLoadPlayer.class)
						.addPattern("set %-~objects% to luckperm[s] user [from] [player] %offlineplayer%")
						.build()
		);
	}

	private Expression<OfflinePlayer> player;
	private Expression<?> variable;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);
		player = (Expression<OfflinePlayer>) expressions[1];
		variable = expressions[0];
		if (!ChangerUtils.acceptsChange(variable, ChangeMode.SET, User.class)) {
			Skript.error(variable.toString(null, Skript.debug()) + " cannot be set to a LuckPerms user.");
			return false;
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		OfflinePlayer player = this.player.getSingle(event);
		if (player == null)
			return;
		User user = LuckPermsProvider.get().getUserManager()
				.loadUser(player.getUniqueId())
				.join();
		variable.change(event, new Object[]{user}, ChangeMode.SET);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("set", variable)
				.append("to luckperms user from", player)
				.toString();
	}

}
