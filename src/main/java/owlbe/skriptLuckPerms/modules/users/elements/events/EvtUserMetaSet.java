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
import owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents.OnUserMetaSet;

public class EvtUserMetaSet extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry registry) {
        syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserMetaSet.class, "User Meta Set")
                .supplier(EvtUserMetaSet::new)
                .addEvent(OnUserMetaSet.class)
                .addPatterns("[luckperm[s]] user meta set")
                .addDescription("""
                Called when the meta of a user is set.
                Only called when meta is SET not unset etc for that case use `user meta remove`
                
                `event-key` = The key e.g. "prefix" or "suffix"
                `event-value` = The result that key was set to.
                
                """)
                .addExample("""
                        on user meta set:
                        	set {_m} to formatted event-value
                        	event-key is "prefix"
                        	send "Your prefix has been updated: %{_m}%" to player
                        """)
                .addSince("1.0")
                .build());
        registry.register(EventValue.builder(OnUserMetaSet.class, String.class)
                .getter(OnUserMetaSet::getValue)
                .patterns("value")
                .build());
        registry.register(EventValue.builder(OnUserMetaSet.class, String.class)
                .getter(OnUserMetaSet::getKey)
                .patterns("key")
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
        return "user meta set";
    }
}

