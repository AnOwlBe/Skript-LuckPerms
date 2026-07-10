package owlbe.skriptLuckPerms.modules.users.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnUserMetaRemove;

public class EvtUserMetaRemove extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserMetaRemove.class, "User Meta Remove")
				.supplier(EvtUserMetaRemove::new)
				.addEvent(OnUserMetaRemove.class)
				.addPattern("[luckperm[s]] user meta remove")
				.addDescription("""
				Called when the meta of a user is removed.
				Note: This event may be called multiple times if you clear a meta.
				
				`event-key` = The key e.g. "prefix" or "suffix"
				`event-value` = The result that key was set to.
				""")
				.addExample("""
						on user meta remove:
							set {_m} to formatted event-value
							event-key is "prefix"
							send "You lost the prefix %{_m}%<reset>!" to player
						""")
				.addSince("1.0")
				.build());

		eventValueRegistry.register(EventValue.builder(OnUserMetaRemove.class, String.class)
				.getter(OnUserMetaRemove::getValue)
				.patterns("value")
				.build());

		eventValueRegistry.register(EventValue.builder(OnUserMetaRemove.class, String.class)
				.getter(OnUserMetaRemove::getKey)
				.patterns("key")
				.build());
	}

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean check(Event event) {
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "user meta remove";
	}

}
