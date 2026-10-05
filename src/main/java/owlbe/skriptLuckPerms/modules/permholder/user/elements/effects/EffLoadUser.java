package owlbe.skriptLuckPerms.modules.permholder.user.elements.effects;

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

import java.util.ArrayList;
import java.util.List;

@Name("Load User")
@Description("""
		Gets and loads a LuckPerms user from the given player.
		""")
@Example("""
		function example(p: offlineplayer):
			set {_lp} to luckperms user from {_p}
			broadcast "%{_p}% has %size of groups of {_lp}% groups!"
		""")
@Since("1.0, 1.0.2 (pattern change), INSERT VERSION (player)")
public class EffLoadUser extends AsyncEffect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffLoadUser.class)
						.addPattern("set %-~objects% to luckperm[s] user [from] [player] %offlineplayers%")
						.supplier(EffLoadUser::new)
						.build()
		);
	}

	private Expression<OfflinePlayer> players;
	private Expression<?> variable;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);

		players = (Expression<OfflinePlayer>) expressions[1];
		variable = expressions[0];

		if (!ChangerUtils.acceptsChange(variable, ChangeMode.SET, User.class)) {
			Skript.error(variable.toString(null, Skript.debug()) + " cannot be set to a LuckPerms user.");
			return false;
		}

		return true;
	}

	@Override
	protected void execute(Event event) {
		OfflinePlayer[] players = this.players.getArray(event);
		if (players == null)
			return;

		List<User> users = new ArrayList<>();

		for (OfflinePlayer player : players) {
			users.add(LuckPermsProvider.get().getUserManager()
					.loadUser(player.getUniqueId())
					.join());
		}

		variable.change(event, users.toArray(new User[0]), ChangeMode.SET);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("set", variable)
				.append("to luckperms user from", players)
				.toString();
	}

}
