package owlbe.skriptLuckPerms.modules.users.elements.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.user.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Has LuckPerms Permission")
@Description(""" 
             Returns whether a user has/doesn't have a permission.
             This is different from Skript's has permission condition as with `user` this supports offline players.
             """)
@Example("""
        function hasPerm(p: player,perm: string) :: boolean:
            get luckperms user {_p} and store it in {_lp}
            if {_lp} has luckperms permission {_perm}:
                return true
            else:
                return false
        """)
@Since("1.0")
public class CondHasPermission extends Condition {
    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.CONDITION,
                SyntaxInfo.builder(CondHasPermission.class)
                        .addPatterns("%luckpermsuser% has luckperm[s] perm[ission] %string%",
                                "%luckpermsuser% (doesn't|does not) have luckperm[s] perm[ission] %string%")
                        .build()
        );
    }
    private Expression<User> userExpr;
    private Expression<String> permExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        userExpr = (Expression<User>) expressions[0];
        permExpr = (Expression<String>) expressions[1];
        setNegated(i == 1);
        return true;
    }

    @Override
    public boolean check(Event event) {
        User user = userExpr.getSingle(event);
        if (user == null) return false;
        String perm = permExpr.getSingle(event);
        if (perm == null) return false;
        return isNegated() != user.getCachedData().getPermissionData().checkPermission(perm).asBoolean();
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append(userExpr)
                .append(isNegated() ? "doesn't have permission" : "has permission")
                .append(permExpr)
                .toString();
    }
}
