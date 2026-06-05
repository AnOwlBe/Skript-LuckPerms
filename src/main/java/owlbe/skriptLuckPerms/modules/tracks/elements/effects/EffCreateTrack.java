package owlbe.skriptLuckPerms.modules.tracks.elements.effects;

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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@SuppressWarnings("unchecked")
@Name("Create Track")
@Description("""
        Creates a new track and then loads it into memory.
        """)
@Example("""
        function example(name: string):
            create new luckperms track named {_name}
        """)
@Since("1.0.2")

public class EffCreateTrack extends AsyncEffect {
    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffCreateTrack.class)
                        .addPatterns("create [new] luckperm[s] track (with name|named) %string%")
                        .build()

        );
    }
    private Expression<String> nameExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        getParser().setHasDelayBefore(Kleenean.TRUE);
        nameExpr = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        String name = nameExpr.getSingle(event);
        if (name == null) return;
        if (!name.matches("[a-z0-9_.-]+")) return; // no public method to check if name is valid for some reason but also names don't support spaces..
        LuckPermsProvider.get().getTrackManager().createAndLoadTrack(name);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("create luckperms track")
                .append(nameExpr)
                .toString();
    }
}
