package owlbe.skriptLuckPerms.modules.users.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnUserDemote;

public class EvtUserDemote extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserDemote.class, "User Demote")
				.supplier(EvtUserDemote::new)
				.addEvent(OnUserDemote.class)
				.addPattern("[luckperm[s]] user demote[d]")
				.addDescription("""
				Called when a group of a player is updated via a demotion on a.
				
				`event-group` = The group the user was demoted to.
				`past event-group` = The group the user had on the track.
				`event-track` = The track that the demotion happened on.
				
				""")
				.addExample("""
						on user demote:
							broadcast "Wow! %player% got demoted from %previous event-group% on track %event-track%!"
						""")
				.addSince("1.0")
				.build());

		eventValueRegistry.register(EventValue.builder(OnUserDemote.class, Group.class)
				.getter(OnUserDemote::getGroup)
				.patterns("group")
				.build());

		eventValueRegistry.register(EventValue.builder(OnUserDemote.class, Group.class)
				.getter(OnUserDemote::getPreviousGroup)
				.patterns("group")
				.time(EventValue.Time.PAST)
				.build());

		eventValueRegistry.register(EventValue.builder(OnUserDemote.class, String.class)
				.getter(OnUserDemote::getTrack)
				.patterns("track")
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
		return "user demoted";
	}

}
