package owlbe.skriptLuckPerms.modules.tracks.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("All Groups In Track")
@Description("""
        Returns a list of groups in the specified track.
        """)
@Example("""
        command /getgroupsintrack:
            trigger:
                 send "Showing list of groups in track  'example'" to player
                 loop all of the groups in track "example":
                     send name of loop-value to player
        """)
@Since("1.0.2")
public class ExprTrackGroups extends SimpleExpression<String> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        syntaxRegistry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprTrackGroups.class, String.class)
                        .addPatterns(
                                "all [of the] [luckperm[s]] groups (of|in) track %luckpermstrack%")
                        .build()
        );
    }

    private Expression<Track> trackExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        trackExpr = (Expression<Track>) expressions[0];
        return true;
    }

    @Override
    protected String[] get(Event event) {
        Track track = trackExpr.getSingle(event);
        if (track == null) return new String[0];
        return track.getGroups().toArray(String[]::new);

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
        return "all users in track";
    }

}
