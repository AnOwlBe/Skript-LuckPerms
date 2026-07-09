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
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@SuppressWarnings("unchecked")
@Name("Delete Track")
@Description("""
        Deletes a luckperms track.
        """)
@Example("""
        function example(name: string):
            delete luckperms track named {_name}
        """)
@Since("1.0.2")
public class EffDeleteTrack extends AsyncEffect {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffDeleteTrack.class)
                        .addPatterns("delete luckperm[s] track %luckpermstrack%")
                        .build()
        );
    }

    private Expression<Track> trackExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        getParser().setHasDelayBefore(Kleenean.TRUE);
        trackExpr = (Expression<Track>) expressions[0];
        return true;
    }

    @Override
    protected void execute(Event event) {
        Track track = trackExpr.getSingle(event);
        if (track == null) return;
        LuckPermsProvider.get().getTrackManager().deleteTrack(track);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("delete luckperms track")
                .append(trackExpr)
                .toString();
    }

}

