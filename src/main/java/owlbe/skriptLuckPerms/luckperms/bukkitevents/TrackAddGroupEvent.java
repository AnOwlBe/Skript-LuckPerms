package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackAddGroupEvent extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final net.luckperms.api.event.track.mutate.TrackAddGroupEvent event;

	public TrackAddGroupEvent(net.luckperms.api.event.track.mutate.TrackAddGroupEvent event) {
		this.event = event;
	}

	public @Nullable Group getGroup() {
		return LuckPermsProvider.get().getGroupManager().getGroup(event.getGroup());
	}

	public @NotNull Track getTrack() {
		return event.getTrack();
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
