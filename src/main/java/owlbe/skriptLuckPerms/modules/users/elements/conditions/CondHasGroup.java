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
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Has LuckPerms Group")
@Description(""" 
             Returns whether a user has/doesn't have a group.
             This is different from Skript's has group condition as with `user` this supports offline players.
             """)
@Example("""
        function hasGroup(p: player,group: string) :: boolean:
            get luckperms user {_p} and store it in {_lp}
            if {_lp} has luckperms group {_group}:
                return true
            else:
                return false
        """)
@Since("1.0")
public class CondHasGroup extends Condition {
    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.CONDITION,
                SyntaxInfo.builder(CondHasGroup.class)
                        .addPatterns("%luckpermsuser% has luckperm[s] group %luckpermsgroup%",
                                "%luckpermsuser% (doesn't|does not) have luckperm[s] group %luckpermsgroup%")
                        .build()
        );
    }
    private Expression<User> userExpr;
    private Expression<Group> groupExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        userExpr = (Expression<User>) expressions[0];
        groupExpr = (Expression<Group>) expressions[1];
        setNegated(i == 1);
        return true;
    }

    @Override
    public boolean check(Event event) {
        User user = userExpr.getSingle(event);
        if (user == null) return false;
        Group group = groupExpr.getSingle(event);
        if (group == null) return false;
        boolean result = user.getNodes(NodeType.INHERITANCE).stream()
                .anyMatch(node -> node.getGroupName().equals(group.getName()));
        return isNegated() != result;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append(userExpr)
                .append(isNegated() ? "doesn't have group" : "has group")
                .append(groupExpr)
                .toString();
    }
}

