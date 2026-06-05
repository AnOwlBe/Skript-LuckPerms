package owlbe.skriptLuckPerms.modules.groups.elements.effects;

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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@SuppressWarnings("unchecked")
@Name("Delete Group")
@Description("""
        Deletes a group.
        """)
@Example("""
        function example(name: string):
            delete luckperms group named {_name}
        """)
@Since("1.0.2")
public class EffDeleteGroup extends AsyncEffect {
    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffDeleteGroup.class)
                        .addPatterns("delete luckperm[s] group %luckpermsgroup%")
                        .build()

        );
    }
    private Expression<Group> groupExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        getParser().setHasDelayBefore(Kleenean.TRUE);
        groupExpr = (Expression<Group>) expressions[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Group group = groupExpr.getSingle(event);
        if (group == null) return;
        LuckPermsProvider.get().getGroupManager().deleteGroup(group);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("delete luckperms group")
                .append(groupExpr)
                .toString();
    }
}
