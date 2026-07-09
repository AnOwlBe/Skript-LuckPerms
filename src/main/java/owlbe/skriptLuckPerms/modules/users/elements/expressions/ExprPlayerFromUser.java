package owlbe.skriptLuckPerms.modules.users.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@SuppressWarnings("unchecked")
@Name("Player From User")
@Description("""
        Returns an offline player from a luckperms user.
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
                        .addPatterns(
                                "[offline]player from [luckperm[s]] user %luckpermsuser%")
                        .build()
        );
    }

    private Expression<User> userExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        userExpr = (Expression<User>) expressions[0];
        return true;
    }

    @Override
    protected OfflinePlayer[] get(Event event) {
       User user = userExpr.getSingle(event);
       if (user == null) return new OfflinePlayer[0];
       OfflinePlayer player = Bukkit.getOfflinePlayer(user.getUniqueId());
       return new OfflinePlayer[]{player};

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
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("player from user")
                .append(userExpr)
                .toString();
    }

}
