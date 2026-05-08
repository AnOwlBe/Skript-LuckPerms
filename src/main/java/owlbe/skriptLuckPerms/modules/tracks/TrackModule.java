package owlbe.skriptLuckPerms.modules.tracks;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.track.Track;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.tracks.elements.expressions.ExprAllTracks;

import javax.annotation.Nullable;
import java.util.List;

public class TrackModule extends HierarchicalAddonModule {

    public TrackModule(AddonModule parentModule) {
        super(parentModule);
    }

    @Override
    public Iterable<AddonModule> children() {
        return List.of();
    }

    @Override
    public void loadSelf(SkriptAddon addon) {
        register(addon,
                ExprAllTracks::register
                );
        Classes.registerClass(new ClassInfo<>(Track.class, "luckpermstrack")
                .user("luckperms ?tracks?")
                .name("LuckPerms Track")
                .description("A LuckPerms track.")
                .parser(new Parser<>() {
                    @Override
                    @Nullable
                    public Track parse(String s, ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(Track track, int i) {
                        return track.getName();
                    }

                    @Override
                    public String toVariableNameString(Track track) {
                        return track.getName();
                    }
                })
                .since("1.0"));
        Converters.registerConverter(Track.class, String.class, Track::getName);
        Converters.registerConverter(String.class, Track.class, name -> LuckPermsProvider.get().getTrackManager().getTrack(name));
    }

    @Override
    public String name() {
        return "track";
    }

}
