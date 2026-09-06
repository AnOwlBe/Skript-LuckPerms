package owlbe.skriptLuckPerms.modules.track.elements.events;

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
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.TrackAddGroupEvent;

public class EvtTrackAddGroup extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtTrackAddGroup.class, "Track Add Group")
				.supplier(EvtTrackAddGroup::new)
				.addEvent(TrackAddGroupEvent.class)
				.addPatterns("[luckperm[s]] group [%-luckpermsgroup%] added to luckperm[s]] track [%-luckpermstrack%]")
				.addDescription("""
				Called when a group is added to a track.
				
				Event Values:
				`event-group` = The group being added to the track.
				`event-track` = The track the group is being added to.
				""")
				.addExample("""
						on track add group:
							broadcast "%event-group% was just added to %event-track%!"
						""")
				.addSince("1.0.2")
				.addSince("INSERT VERSION (pattern rewrite)")
				.build());

		eventValueRegistry.register(EventValue.builder(TrackAddGroupEvent.class, Group.class)
				.getter(TrackAddGroupEvent::getGroup)
				.patterns("group")
				.build());

		eventValueRegistry.register(EventValue.builder(TrackAddGroupEvent.class, Track.class)
				.getter(TrackAddGroupEvent::getTrack)
				.patterns("track")
				.build());
	}

	private @Nullable Expression<Group> group;
	private @Nullable Expression<Track> track;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (args[0] != null)
			this.group = (Expression<Group>) args[0];

		if (args[1] != null)
			track = (Expression<Track>) args[0];

		return true;
	}

	@Override
	public boolean check(Event event) {
		TrackAddGroupEvent trackEvent = (TrackAddGroupEvent) event;

		boolean groupMatched = true;
		boolean trackMatched = true;

		if (group != null)
			groupMatched = group.check(event, group -> group.equals(trackEvent.getGroup()));

		if (track != null)
			trackMatched = track.check(event, track -> track.equals(trackEvent.getTrack()));

		return groupMatched && trackMatched;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("luckperms group")
				.appendIf(group != null, group)
				.append("added to")
				.append("luckperms track")
				.appendIf(track != null, track)
				.toString();
	}

}
