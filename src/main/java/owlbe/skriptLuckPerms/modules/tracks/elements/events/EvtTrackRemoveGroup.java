package owlbe.skriptLuckPerms.modules.tracks.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents.OnTrackRemoveGroup;

public class EvtTrackRemoveGroup extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtTrackRemoveGroup.class, "Track Remove Group")
                .supplier(EvtTrackRemoveGroup::new)
                .addEvent(OnTrackRemoveGroup.class)
                .addPatterns("[luckperm[s]] track remove group")
                .addDescription("""
                Called when a group is remove from a track.
                Only called when a group is removed not added for that case use `on track add group`
                
                `event-group` = The group being removed from the track.
                `event-track` = The track the group is being removed from.
                
                """)
                .addExample("""
                        on track remove group:
                        	broadcast "%event-group% was just removed from %event-track%!"
                        """)
                .addSince("1.0.2")
                .build());
        registry.register(EventValue.builder(OnTrackRemoveGroup.class, Group.class)
                .getter(OnTrackRemoveGroup::getGroup)
                .patterns("group")
                .build());
        registry.register(EventValue.builder(OnTrackRemoveGroup.class, Track.class)
                .getter(OnTrackRemoveGroup::getTrack)
                .patterns("track")
                .build());
    }

    @Override
    public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "track remove group";
    }
}
