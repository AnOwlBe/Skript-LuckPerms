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
import net.luckperms.api.node.Node;
import net.luckperms.api.node.matcher.NodeMatcher;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.UUID;

@SuppressWarnings("unchecked")
@Name("Permission Members")
@Description(""" 
             Returns a list of UUIDS of users who have the specified permission.
             Should be relatively fast depending on how many users have said permission.
             """)
@Example("""
        function search():
            get the users with perm "example" and store it in {_lp::*}
            set {_lp::*} to all luckperms users with permission "example"
            send "%size of {_lp::*}% have 'example' permission!" to all ops
        """)
@Since("1.0")
public class EffPermissionMembers extends AsyncEffect {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffPermissionMembers.class)
                        .addPatterns("set %-~objects% to (all|all of the) [luckperm[s]] (users|players) with perm[ission] %string%")
                        .build()
        );
    }

    private Expression<String> permExpr;
    private Expression<?> varExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        getParser().setHasDelayBefore(Kleenean.TRUE);
        permExpr = (Expression<String>) expressions[1];
        varExpr = expressions[0];
        if (!Changer.ChangerUtils.acceptsChange(varExpr, Changer.ChangeMode.SET, UUID.class)) {
            Skript.error(varExpr.toString(null, Skript.debug()) + " cannot be set to UUIDS.");
            return false;
        }
        return true;
    }

    @Override
    protected void execute(Event event) {
        String perm = permExpr.getSingle(event);
        if (perm == null) return;
        var results = LuckPermsProvider.get().getUserManager()
                .searchAll(NodeMatcher.key(Node.builder(perm).build()))
                .join();
        varExpr.change(event, results.keySet().toArray(), Changer.ChangeMode.SET);

    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("set")
                .append(varExpr)
                .append("to all players with permission")
                .append(permExpr)
                .toString();
    }

}
