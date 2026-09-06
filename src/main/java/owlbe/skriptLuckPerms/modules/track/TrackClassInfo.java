package owlbe.skriptLuckPerms.modules.track;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.track.Track;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.ContainsHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;

import static org.skriptlang.skript.lang.properties.Property.CONTAINS;
import static org.skriptlang.skript.lang.properties.Property.NAME;
import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class TrackClassInfo extends ClassInfo<Track> {

	public TrackClassInfo() {
		super(Track.class, "luckpermstrack");
		this.user("luckperms ?tracks?")
				.name("LuckPerms Track")
				.description("Represents a LuckPerms track.")
				.since("1.0")
				.parser(new TrackParser())
				.defaultExpression(new EventValueExpression<>(Track.class))
				.property(NAME,
						"The name of this track.",
						addon,
						new TrackNameHandler())
				.property(CONTAINS,
						"Checks whether this track contains the given group.",
						addon,
						new TrackContainsHandler());
	}

	private static class TrackParser extends Parser<Track> {
		//<editor-fold desc="track parser" defaultstate="collapsed">
		@Override
		public @Nullable Track parse(String string, ParseContext context) {
			if (context == ParseContext.COMMAND || context == ParseContext.PARSE) {
				if (string.isEmpty())
					return null;
			}
			return LuckPermsProvider.get().getTrackManager().getTrack(string);
		}

		@Override
		public String toString(Track track, int flags) {
			return "track '" + track.getName() + "'";
		}

		@Override
		public String toVariableNameString(Track track) {
			return track.getName();
		}

		//</editor-fold>
	}

	private static class TrackNameHandler implements ExpressionPropertyHandler<Track, String> {
		//<editor-fold desc="track name handler" defaultstate="collapsed">

		@Override
		public @Nullable String convert(Track track) {
			return track.getName();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

	private static class TrackContainsHandler implements ContainsHandler<Track, Group> {
		//<editor-fold desc="track contains handler" defaultstate="collapsed">
		@Override
		public boolean contains(Track track, Group group) {
			return track.containsGroup(group);
		}

		@Override
		public Class<? extends Group>[] elementTypes() {
			return new Class[]{Group.class};
		}

		//</editor-fold>
	}

}
