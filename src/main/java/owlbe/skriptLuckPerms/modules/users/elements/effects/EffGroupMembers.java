package owlbe.skriptLuckPerms.modules.users.elements.effects;

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
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.matcher.NodeMatcher;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unchecked")
@Name("Group Members")
@Description(""" 
             Returns a list of UUIDS of users who have the specified group.
             Should be relatively fast depending on how many users have a group.
             """)
@Example("""
        function search():
            get the members of group "example" and store it in {_lp::*}
            send "%size of {_lp::*}% have 'example' group!" to all ops
        """)
@Since("1.0")

public class EffGroupMembers extends AsyncEffect {
    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffGroupMembers.class)
                        .addPatterns("(fetch|get) [the] [luckperm[s]] (members|players) (of|in) group %luckpermsgroup% and store (it|the result) in %-~objects%")
                        .build()
        );
    }

    private Expression<Group> groupExpr;
    private Expression<?> varExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        getParser().setHasDelayBefore(Kleenean.TRUE);
        groupExpr = (Expression<Group>) expressions[0];
        varExpr = expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Group group = groupExpr.getSingle(event);
        if (group == null) return;
        Map<UUID, Collection<Node>> results = LuckPermsProvider.get().getUserManager()
                .searchAll(NodeMatcher.key((Node) InheritanceNode.builder(group).build()))
                .join();
        varExpr.change(event, results.keySet().toArray(), Changer.ChangeMode.SET);

    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("get members of group")
                .append(groupExpr)
                .append("and store in")
                .append(varExpr)
                .toString();
    }
}


