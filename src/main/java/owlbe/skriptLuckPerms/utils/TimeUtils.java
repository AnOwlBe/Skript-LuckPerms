package owlbe.skriptLuckPerms.utils;

import ch.njol.skript.util.Timespan;

import java.time.Duration;
import java.time.Instant;

/**
 * Utility for working with {@link Timespan}'s
 */
public final class TimeUtils {

	private TimeUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	public static Timespan fromInstant(Instant instant) {
		Duration remaining = Duration.between(Instant.now(), instant);
		if (remaining.isNegative())
			remaining = Duration.ZERO;

		return new Timespan(remaining.toMillis());
	}

	public static Timespan fromDuration(Duration duration) {
		return new Timespan(duration.toMillis());
	}

	public static Instant toInstant(Timespan timespan) {
		return Instant.now().plusMillis(timespan.getDuration().toMillis());
	}

	public static Duration toDuration(Timespan timespan) {
		return timespan.getDuration();
	}
}
