package owlbe.skriptLuckPerms.modules.groups.elements.events;

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
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnGroupReceivePermission;

public class EvtGroupReceivePermission extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtGroupReceivePermission.class, "Group Receive Permission")
                .supplier(EvtGroupReceivePermission::new)
                .addEvent(OnGroupReceivePermission.class)
                .addPatterns("[luckperm[s]] group receive perm[ission]")
                .addDescription("""
                Called when a group receives a permission.
                If the permission duration is infinite %event-timespan% will return 0 seconds.
                
                `event-permission` = The permission the group received.
                `event-group` = The group that received the permission.
                `event-timespan` = The duration the group will have the permission for.
                """)
                .addExample("""
                        on group receive permission:
                        	broadcast "Wow! %event-group% just got permission %event-permission%!" to player
                        """)
                .addSince("1.0")
                .build());
        registry.register(EventValue.builder(OnGroupReceivePermission.class, String.class)
                .getter(OnGroupReceivePermission::getPermission)
                .patterns("permission")
                .build());

        registry.register(EventValue.builder(OnGroupReceivePermission.class, Group.class)
                .getter(OnGroupReceivePermission::getGroup)
                .patterns("group")
                .build());

        registry.register(EventValue.builder(OnGroupReceivePermission.class, Timespan.class)
                .getter(OnGroupReceivePermission::getDuration)
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
        return "group receive permission";
    }

}
