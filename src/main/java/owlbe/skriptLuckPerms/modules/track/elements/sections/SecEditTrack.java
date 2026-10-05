package owlbe.skriptLuckPerms.modules.track.elements.sections;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.SkriptLuckPerms;

import java.util.Arrays;
import java.util.List;

@Name("Edit LuckPerms Track")
@Description("""
		Creates a section that allows you to modify the properties of the provided track(s).
		After the code in the section has finished the track(s) will be saved asynchronously.
		
		Event Values:
		`event-track` = The track that is being modified.
		`event-tracks` = The track(s) that are being modified, if more than 1 is being edited.
		""")
@Example("""
		function addToStaffTrack(group: string:
		    edit luckperms track "staff":
		        add arg-1 to luckperms groups of event-track
	""")
@Since("INSERT VERSION")
public class SecEditTrack extends Section {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.SECTION,
				SyntaxInfo.builder(SecEditTrack.class)
						.addPattern("edit [the] luckperm[s] track %luckpermstracks%")
						.build()
		);

		eventValueRegistry.register(EventValue.builder(TrackSectionEvent.class, Track[].class)
				.getter(TrackSectionEvent::getTracks)
				.patterns("tracks")
				.build());

		eventValueRegistry.register(EventValue.builder(TrackSectionEvent.class, Track.class)
				.getter(event -> event.getTracks()[0])
				.patterns("track")
				.build());
	}

	private Expression<Track> tracks;

	private @Nullable Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult,
						@Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		getParser().setHasDelayBefore(Kleenean.TRUE);

		tracks = (Expression<Track>) expressions[0];

		if (sectionNode != null) {
			trigger = SectionUtils.loadLinkedCode("edit track section", (beforeLoading, afterLoading)
					-> loadCode(sectionNode, "edit track section", beforeLoading, afterLoading, TrackSectionEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		if (trigger != null) {
			Track[] tracks = this.tracks.getArray(event);
			if (tracks == null)
				return null;

			TrackSectionEvent sectionEvent = new TrackSectionEvent(tracks);

			Object variables = Variables.copyLocalVariables(event);
			Variables.setLocalVariables(sectionEvent, variables);
			TriggerItem.walk(trigger, sectionEvent);

			Bukkit.getScheduler().runTaskAsynchronously(SkriptLuckPerms.getPluginInstance(), () -> {
				for (Track track : tracks)
					LuckPermsProvider.get().getTrackManager().saveTrack(track);

				Bukkit.getScheduler().runTask(SkriptLuckPerms.getPluginInstance(), () -> {
					Variables.setLocalVariables(event, Variables.copyLocalVariables(sectionEvent));
					Variables.removeLocals(sectionEvent);
					Variables.removeLocals(event);
					TriggerItem.walk(getNext(), sectionEvent);
				});

			});
		}

		return super.walk(event, false);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "edit luckperms tracks " + tracks.toString(event, debug);
	}

	public static class TrackSectionEvent extends Event {

		private final Track[] tracks;

		public TrackSectionEvent(Track... track) {
			this.tracks = track;
		}

		public Track[] getTracks() {
			return this.tracks;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}
