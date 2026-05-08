package owlbe.skriptLuckPerms.modules.users.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents.OnUserPromote;

public class EvtUserPromote extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserPromote.class, "User Promote")
                .supplier(EvtUserPromote::new)
                .addEvent(OnUserPromote.class)
                .addPatterns("[luckperm[s]] user promote[d]")
                .addDescription("""
                Called when a group of a player is updated via a promotion on a track (/lp user notch promote staff)")
                Do note that %past event-group% will be unset if player didn't previously have a group on that track.
                """)
                .addExample("""
                        on user promoted:
                        	broadcast "Wow! %player% got promoted to %event-group% (previous group was %past event-group% along track %event-track%"
                        """)
                .addSince("1.0")
                .build());
        registry.register(EventValue.builder(OnUserPromote.class, Group.class)
                .getter(OnUserPromote::getGroup)
                .patterns("group")
                .build());
        registry.register(EventValue.builder(OnUserPromote.class, Group.class)
                .getter(OnUserPromote::getPreviousGroup)
                .patterns("group")
                .time(EventValue.Time.PAST)
                .build());
        registry.register(EventValue.builder(OnUserPromote.class, String.class)
                .getter(OnUserPromote::getTrack)
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
        return "user promoted";
    }
}
