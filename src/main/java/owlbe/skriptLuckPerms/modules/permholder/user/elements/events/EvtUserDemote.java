package owlbe.skriptLuckPerms.modules.permholder.user.elements.events;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue.Time;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.UserDemoteEvent;

public class EvtUserDemote extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserDemote.class, "User Demote")
				.supplier(EvtUserDemote::new)
				.addEvent(UserDemoteEvent.class)
				.addPattern("[luckperm[s]] user demoted [(along|on) [luckperm[s]] track %-luckpermstrack%]")
				.addDescription("""
				Called when a user is demoted on a track.
				
				Event Values:
				`event-group` = The group the user was demoted to.
				`past event-group` = The group the user had on the track.
				`event-track` = The track that the demotion happened on.
				""")
				.addExample("""
						on user demote:
							broadcast "Wow! %player% got demoted from %previous event-group% on track %event-track%!"
						""")
				.addSince("1.0")
				.addSince("INSERT VERSION (user demoted on track \"example\")")
				.build());

		eventValueRegistry.register(EventValue.builder(UserDemoteEvent.class, Group.class)
				.getter(UserDemoteEvent::getGroup)
				.patterns("group")
				.build());

		eventValueRegistry.register(EventValue.builder(UserDemoteEvent.class, Group.class)
				.getter(UserDemoteEvent::getPreviousGroup)
				.patterns("group")
				.time(Time.PAST)
				.build());

		eventValueRegistry.register(EventValue.builder(UserDemoteEvent.class, Track.class)
				.getter(UserDemoteEvent::getTrack)
				.patterns("track")
				.build());
	}

	private Expression<Track> track;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (args[0] != null)
			track = ((Expression<Track>) args[0]);

		return true;
	}

	@Override
	public boolean check(Event event) {
		UserDemoteEvent userEvent = (UserDemoteEvent) event;

		boolean trackMatched = true;

		if (this.track != null) {
			trackMatched = this.track.check(event, track -> track.equals(userEvent.getTrack()));
		}

		return trackMatched;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("user demoted")
				.appendIf(track != null, "on track ", track)
				.toString();
	}

}
