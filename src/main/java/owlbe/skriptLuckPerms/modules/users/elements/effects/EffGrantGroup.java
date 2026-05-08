package owlbe.skriptLuckPerms.modules.users.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

import java.time.Duration;

@SuppressWarnings("unchecked")
@Name("Grant Group")
@Description("Adds a group to a user.")
@Example("""
function example(p: offlineplayer,group: string,duration: timespan=0 seconds):
    get luckperms user {_p} and store it in {_lp}
    if {_duration} is not 0 seconds:
        edit user {_lp}:
            grant luckperms group {_group} for {_duration}
        send "You just got %{_group}% group for %{_duration}%!" to {_p}
    else:
        edit user {_lp}:
            grant luckperms group {_group}
            send "You just got %{_group}% permission!" to {_p}
        """)
@Since("1.0")

public class EffGrantGroup extends Effect {
    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffGrantGroup.class)
                        .addPatterns("(grant|add) luckperm[s] group %luckpermsgroup% [to %-luckpermsuser%] [for %-timespan%]")
                        .build()
        );
    }

    private Expression<Group> groupExpr;
    private Expression<User> userExpr;
    private Expression<Timespan> durationExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(SecEditUser.UserEvent.class)) {
            Skript.error("This can only be used inside an 'edit user' section");
            return false;
        }
        groupExpr = (Expression<Group>) expressions[0];
        userExpr = expressions[1] != null ? (Expression<User>) expressions[1] : null;
        durationExpr = (Expression<Timespan>) expressions[2];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Group group = groupExpr.getSingle(event);
        User user = userExpr != null ? userExpr.getSingle(event) : ((SecEditUser.UserEvent) event).getUser();
        if (group == null || user == null) return;
        Timespan duration = durationExpr != null ? durationExpr.getSingle(event) : null;
        if (duration != null) {
            user.data().add(InheritanceNode.builder(group)
                    .expiry(Duration.ofSeconds(duration.getAs(Timespan.TimePeriod.SECOND)))
                    .build());
        } else {
            user.data().add(InheritanceNode.builder(group).build());
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("grant group")
                .append(groupExpr)
                .append("to")
                .append(userExpr)
                .appendIf(durationExpr != null, "for", durationExpr)
                .toString();
    }
}


