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
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnTrackAddGroup;

public class EvtTrackAddGroup extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtTrackAddGroup.class, "Track Add Group")
                .supplier(EvtTrackAddGroup::new)
                .addEvent(OnTrackAddGroup.class)
                .addPatterns("[luckperm[s]] track add group")
                .addDescription("""
                Called when a group is added to a track.
                Only called when a group is added not removed for that case use `on track remove group`
                
                `event-group` = The group being added to the track.
                `event-track` = The track the group is being added to.
                
                """)
                .addExample("""
                        on track add group:
                        	broadcast "%event-group% was just added to %event-track%!"
                        """)
                .addSince("1.0.2")
                .build());

        registry.register(EventValue.builder(OnTrackAddGroup.class, Group.class)
                .getter(OnTrackAddGroup::getGroup)
                .patterns("group")
                .build());

        registry.register(EventValue.builder(OnTrackAddGroup.class, Track.class)
                .getter(OnTrackAddGroup::getTrack)
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
        return "track add group";
    }

}
