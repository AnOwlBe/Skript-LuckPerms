package owlbe.skriptLuckPerms.modules.users.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
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

@SuppressWarnings("unchecked")
@Name("Load Player")
@Description("""
        Loads a LuckPerms user.
        This is required for basically everything relating to users.
        """)
@Example("""
        function example(p: offlineplayer):
            get luckperms user {_p} and store it in {_lp}
            broadcast "%{_p}% has %size of groups of {_lp}% groups!"
        """)
@Since("1.0")

public class EffLoadPlayer extends AsyncEffect {
    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffLoadPlayer.class)
                        .addPatterns("(fetch|get) luckperm[s] user [from] %offlineplayer% and store (it|the result) in %-~objects%")
                        .build()

        );
    }

    private Expression<OfflinePlayer> playerExpr;
    private Expression<?> varExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        getParser().setHasDelayBefore(Kleenean.TRUE);
        playerExpr = (Expression<OfflinePlayer>) expressions[0];
        varExpr = expressions[1];
        if (!Changer.ChangerUtils.acceptsChange(varExpr, Changer.ChangeMode.SET, User.class)) {
            Skript.error(varExpr.toString(null, Skript.debug()) + " cannot be set to a LuckPerms user.");
            return false;
        }
        return true;
    }

    @Override
    protected void execute(Event event) {
        OfflinePlayer player = playerExpr.getSingle(event);
        if (player == null) return;
        User user = LuckPermsProvider.get().getUserManager()
                .loadUser(player.getUniqueId())
                .join();
        varExpr.change(event, new Object[]{user}, Changer.ChangeMode.SET);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("get luckperms user from")
                .append(playerExpr)
                .append("and store in")
                .append(varExpr)
                .toString();
    }
}

