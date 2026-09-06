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

import java.util.List;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

@Name("Edit LuckPerms Track")
@Description("""
		Creates a section that allows you to modify the properties of the provided track.
		After the code in the section has finished the track will be saved asynchronously.
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
						.addPattern("edit [the] luckperm[s] track %luckpermstrack%")
						.build()
		);

		eventValueRegistry.register(EventValue.builder(TrackSectionEvent.class, Track.class)
				.getter(TrackSectionEvent::getTrack)
				.patterns("track")
				.build());
	}

	private Expression<Track> track;

	private @Nullable Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult,
						@Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		track = (Expression<Track>) expressions[0];

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
			Track track = this.track.getSingle(event);
			if (track == null)
				return null;

			TrackSectionEvent sectionEvent = new TrackSectionEvent(track);

			Object variables = Variables.copyLocalVariables(event);
			Variables.setLocalVariables(sectionEvent, variables);
			TriggerItem.walk(trigger, sectionEvent);

			Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
				LuckPermsProvider.get().getTrackManager().saveTrack(track);

				Bukkit.getScheduler().runTask(instance, () -> {
					Variables.setLocalVariables(event, Variables.copyLocalVariables(sectionEvent));
					Variables.removeLocals(sectionEvent);
					Variables.removeLocals(event);
				});

			});
		}

		return super.walk(event, false);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "edit luckperms track " + track.toString(event, debug);
	}

	public static class TrackSectionEvent extends Event {

		private final Track track;

		public TrackSectionEvent(Track track) {
			this.track = track;
		}

		public Track getTrack() {
			return this.track;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}
