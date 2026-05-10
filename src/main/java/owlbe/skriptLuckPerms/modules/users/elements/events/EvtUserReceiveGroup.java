package owlbe.skriptLuckPerms.modules.users.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents.OnUserReceiveGroup;

public class EvtUserReceiveGroup extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserReceiveGroup.class, "User Receive Group")
                .supplier(EvtUserReceiveGroup::new)
                .addEvent(OnUserReceiveGroup.class)
                .addPatterns("[luckperm[s]] user receive group")
                .addDescription("""
                Called when a user receives a group.
                If the group duration is infinite %event-timespan% will return 0 seconds.
                
                `event-group` = The group the user received.
                `event-timespan` = The duration the user will have the group for.
                
                """)
                .addExample("""
                        on user receive group:
                        	send "You just received %event-group% for %event-duration%!" to player
                        """)
                .addSince("1.0")
                .build());
        registry.register(EventValue.builder(OnUserReceiveGroup.class, Group.class)
                .getter(OnUserReceiveGroup::getGroup)
                .patterns("group")
                .build());
        registry.register(EventValue.builder(OnUserReceiveGroup.class, Timespan.class)
                .getter(OnUserReceiveGroup::getDuration)
                .patterns("timespan")
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
        return "user receive group";
    }
}


