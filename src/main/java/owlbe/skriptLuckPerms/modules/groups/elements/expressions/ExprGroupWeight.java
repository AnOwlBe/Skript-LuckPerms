package owlbe.skriptLuckPerms.modules.groups.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.WeightNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.groups.elements.sections.SecEditGroup;

@SuppressWarnings("unchecked")
@Name("Permissions Of")
@Description("""
        Represents the permissions of a user.
        """)
@Example("""
        function example(p: offlineplayer):
            get luckperms user {_p} and store it in {_lp}
            broadcast "%{_p}% has %size of luckperms permissions of {_lp}% permissions!%
            broadcast "their groups: %luckperm permissions of {_lp}%"
        """)
@Since("1.0")

public class ExprGroupWeight extends SimpleExpression<Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        syntaxRegistry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGroupWeight.class, Integer.class)
                        .addPatterns(
                                "[luckperm[s]] group weight [of] [group] [%-luckpermsgroup%]")
                        .build()
        );
    }

    private Expression<Group> groupExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        groupExpr = expressions[0] != null ? (Expression<Group>) expressions[0] : new EventValueExpression<>(Group.class);
        return true;
    }

    @Override
    protected Integer[] get(Event event) {
        Group group = groupExpr.getSingle(event);
        if (group == null) return new Integer[0];
        int weight = group.getWeight().orElse(0);
        return new Integer[]{weight};
    }
    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        if (!getParser().isCurrentEvent(SecEditGroup.GroupEvent.class)) {
            Skript.error("This can only be used inside an 'edit group' section");
            return null;
        }
        return switch (mode) {
            case SET, ADD, RESET -> CollectionUtils.array(Integer.class);
            default -> null;
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        Group group = groupExpr.getSingle(event);
        if (group == null) return;
        if (mode == Changer.ChangeMode.RESET) {
            group.data().clear(NodeType.WEIGHT::matches);
        } else {
            if (delta == null) return;
            Integer weight = (Integer) delta[0];
            if (mode == Changer.ChangeMode.SET) {
                group.data().clear(NodeType.WEIGHT::matches);
                group.data().add(WeightNode.builder(weight).build());
            } else if (mode == Changer.ChangeMode.ADD) {
                int current = group.getWeight().orElse(0);
                group.data().clear(NodeType.WEIGHT::matches);
                group.data().add(WeightNode.builder(current + weight).build());
            }
        }
        // Don't save group here its saved in the section async
    }
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("group weight of")
                .append(groupExpr)
                .toString();
    }
}

