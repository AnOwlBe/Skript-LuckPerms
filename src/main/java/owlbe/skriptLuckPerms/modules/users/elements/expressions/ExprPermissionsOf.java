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
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@SuppressWarnings("unchecked")
@Name("Permissions Of")
@Description("""
        Represents the permissions of a user.
        """)
@Example("""
        function example(p: offlineplayer):
            get luckperms user {_p} and store it in {_lp}
            broadcast "%{_p}% has %size of luckperms permissions of {_lp}% permissions!%
            broadcast "their permissions: %luckperms p ermissions of {_lp}%"
        """)
@Since("1.0")

public class ExprPermissionsOf extends SimpleExpression<String> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        syntaxRegistry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprPermissionsOf.class, String.class)
                        .addPatterns(
                                "[luckperm[s]] (perms|permissions) of %luckpermsusers%",
                                "%luckpermsusers%'s luckperm[s] (perms|permissions)")
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
    protected String[] get(Event event) {
        User user = userExpr.getSingle(event);
        if (user == null) return new String[0];
        return user.getNodes(NodeType.PERMISSION)
                .stream()
                .map(Node::getKey)
                .toArray(String[]::new);
    }
    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("permissions of")
                .append(userExpr)
                .toString();
    }
}

