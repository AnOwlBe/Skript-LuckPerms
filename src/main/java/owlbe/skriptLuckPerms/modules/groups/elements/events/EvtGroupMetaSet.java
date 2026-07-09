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
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnGroupMetaSet;

public class EvtGroupMetaSet extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtGroupMetaSet.class, "Group Meta Set")
                .supplier(EvtGroupMetaSet::new)
                .addEvent(OnGroupMetaSet.class)
                .addPatterns("[luckperm[s]] group meta set")
                .addDescription("""
                Called when the meta of a group is set.
                Only called when meta is SET not unset etc for that case use `group meta remove`
                
                `event-key` = The key e.g. "prefix" or "suffix"
                `event-value` = The result that key was set to.
                `event-group` = The group that received the meta.
                
                """)
                .addExample("""
                        on group meta set:
                        	set {_m} to formatted event-value
                        	event-key is "prefix"
                        	broadcast "%event-group%'s prefix has been updated: %{_m}%"
                        """)
                .addSince("1.0")
                .build());

        registry.register(EventValue.builder(OnGroupMetaSet.class, String.class)
                .getter(OnGroupMetaSet::getValue)
                .patterns("value")
                .build());

        registry.register(EventValue.builder(OnGroupMetaSet.class, String.class)
                .getter(OnGroupMetaSet::getKey)
                .patterns("key")
                .build());

        registry.register(EventValue.builder(OnGroupMetaSet.class, Group.class)
                .getter(OnGroupMetaSet::getGroup)
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
        return "group meta set";
    }

}
