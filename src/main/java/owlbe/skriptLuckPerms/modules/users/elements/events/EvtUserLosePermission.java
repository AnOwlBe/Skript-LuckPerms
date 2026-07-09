package owlbe.skriptLuckPerms.modules.users.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnUserLosePermission;

public class EvtUserLosePermission extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserLosePermission.class, "User Lose Permission")
                .supplier(EvtUserLosePermission::new)
                .addEvent(OnUserLosePermission.class)
                .addPatterns("[luckperm[s]] user lose perm[ission]")
                .addDescription("""
                Called when a user loses a permission.
                
                `event-permission` = The permission the user lost.
                
                """)
                .addExample("""
                        on user lose permission:
                        	send "You just lost %event-permission%!" to player
                        """)
                .addSince("1.0")
                .build());

        registry.register(EventValue.builder(OnUserLosePermission.class, String.class)
                .getter(OnUserLosePermission::getPermission)
                .patterns("permission")
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
        return "user lose permission";
    }

}
