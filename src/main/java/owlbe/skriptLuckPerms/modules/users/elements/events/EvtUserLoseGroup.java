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
import owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents.OnUserLoseGroup;

public class EvtUserLoseGroup extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserLoseGroup.class, "User Lose Group")
                .supplier(EvtUserLoseGroup::new)
                .addEvent(OnUserLoseGroup.class)
                .addPatterns("[luckperm[s]] user lose group")
                .addDescription("""
                Called when a user loses a group.
                
                `event-group` = The group the user lost.
                
                """)
                .addExample("""
                        on user lose group:
                        	send "You just lost %event-group% ;o" to player
                        """)
                .addSince("1.0")
                .build());
        registry.register(EventValue.builder(OnUserLoseGroup.class, Group.class)
                .getter(OnUserLoseGroup::getGroup)
                .patterns("group")
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
        return "user lose group";
    }
}



