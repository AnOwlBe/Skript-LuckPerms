package owlbe.skriptLuckPerms.utils.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * For sections that need an event
 */
public abstract class SectionEvent extends Event {

	@Override
	public @NotNull HandlerList getHandlers() {
		throw new IllegalStateException();
	}

}
