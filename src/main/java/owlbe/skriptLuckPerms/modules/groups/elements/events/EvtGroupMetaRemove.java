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
import owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents.OnGroupMetaRemove;

public class EvtGroupMetaRemove extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtGroupMetaRemove.class, "Group Meta Remove")
                .supplier(EvtGroupMetaRemove::new)
                .addEvent(OnGroupMetaRemove.class)
                .addPatterns("[luckperm[s]] group meta remove")
                .addDescription("""
                Called when the meta of a group is removed.
                Only called when meta is REMOVED not set/added to for that case use `group meta set`
                
                `event-key` = The key e.g. "prefix" or "suffix"
                `event-value` = The result that key was set to.
                `event-group` = The group that lost said meta.
                
                """)
                .addExample("""
                        on group meta remove:
                        	set {_m} to formatted event-value
                        	event-key is "prefix"
                        	broadcast "%event-group% no longer has prefix %{_m}%<white>!"
                        """)
                .addSince("1.0")
                .build());
        registry.register(EventValue.builder(OnGroupMetaRemove.class, String.class)
                .getter(OnGroupMetaRemove::getValue)
                .patterns("value")
                .build());
        registry.register(EventValue.builder(OnGroupMetaRemove.class, String.class)
                .getter(OnGroupMetaRemove::getKey)
                .patterns("key")
                .build());
        registry.register(EventValue.builder(OnGroupMetaRemove.class, Group.class)
                .getter(OnGroupMetaRemove::getGroup)
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
        return "group meta remove";
    }
}



