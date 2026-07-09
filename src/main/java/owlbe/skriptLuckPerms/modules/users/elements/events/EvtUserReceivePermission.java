package owlbe.skriptLuckPerms.modules.users.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnUserReceivePermission;

public class EvtUserReceivePermission extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserReceivePermission.class, "User Receive Permission")
                .supplier(EvtUserReceivePermission::new)
                .addEvent(OnUserReceivePermission.class)
                .addPatterns("[luckperm[s]] user receive perm[ission]")
                .addDescription("""
                Called when a user receives a permission.
                If the permission duration is infinite %event-timespan% will return 0 seconds.
                
                `event-permission` = The permission that the user received.
                `event-timespan` = The duration the user will have the permission for.
                
                """)
                .addExample("""
                        on user receive permission:
                        	send "You just got the permission %event-permission% for %event-timespan%" to player
                        """)
                .addSince("1.0")
                .build());

        registry.register(EventValue.builder(OnUserReceivePermission.class, String.class)
                .getter(OnUserReceivePermission::getPermission)
                .patterns("permission")
                .build());

        registry.register(EventValue.builder(OnUserReceivePermission.class, Timespan.class)
                .getter(OnUserReceivePermission::getDuration)
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
        return "user receive permission";
    }

}
