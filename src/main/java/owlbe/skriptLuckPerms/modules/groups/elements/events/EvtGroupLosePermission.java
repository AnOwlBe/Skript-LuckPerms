package owlbe.skriptLuckPerms.modules.groups.elements.events;

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
import owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents.OnGroupLosePermission;

public class EvtGroupLosePermission extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtGroupLosePermission.class, "Group Lose Permission")
                .supplier(EvtGroupLosePermission::new)
                .addEvent(OnGroupLosePermission.class)
                .addPatterns("[luckperm[s]] group lose perm[ission]")
                .addDescription("""
                Called when a group loses a permission.
                
                `event-permission` = The permission that was removed.
                `event-group` = The group that lost the permission.
                """)
                .addExample("""
                        on group lose permission:
                        	broadcast "Wow! %event-group% just lost permission %event-permission%!" to player
                        """)
                .addSince("1.0")
                .build());
        registry.register(EventValue.builder(OnGroupLosePermission.class, String.class)
                .getter(OnGroupLosePermission::getPermission)
                .patterns("permission")
                .build());
        registry.register(EventValue.builder(OnGroupLosePermission.class, Group.class)
                .getter(OnGroupLosePermission::getGroup)
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
        return "user lose permission";
    }
}



