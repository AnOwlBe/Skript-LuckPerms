package owlbe.skriptLuckPerms.modules.groups.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("All Groups")
@Description("""
        returns a list of all groups.
        This expression will require `luckperms` in it until Skript deprecates their `groups of`.
        """)
@Example("""
        command /getallgroups:
            trigger:
                 send all of the luckperms groups to player
        """)
@Since("1.0")

public class ExprAllGroups extends SimpleExpression<String> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        syntaxRegistry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprAllGroups.class, String.class)
                        .addPatterns(
                                "all [of the] luckperm[s] groups")
                        .build()
        );
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected String[] get(Event event) {
        return LuckPermsProvider.get().getGroupManager().getLoadedGroups().stream()
                .map(Group::getName)
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
        return "all groups";
    }
}

