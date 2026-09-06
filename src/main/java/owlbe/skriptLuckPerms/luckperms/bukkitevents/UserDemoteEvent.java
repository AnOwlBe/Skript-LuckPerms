package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.track.Track;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.parent.UserEvent;

/**
 * Called when a {@link net.luckperms.api.model.user.User user} is demoted along a track.
 */
public class UserDemoteEvent extends UserEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final net.luckperms.api.event.user.track.UserDemoteEvent event;

	public UserDemoteEvent(OfflinePlayer player, net.luckperms.api.event.user.track.UserDemoteEvent event) {
		super(player);
		this.event = event;
	}

	public @NotNull Track getTrack() {
		return event.getTrack();
	}

	public @Nullable Group getPreviousGroup() {
		return event.getGroupFrom()
				.map(name -> LuckPermsProvider.get().getGroupManager().getGroup(name))
				.orElse(null);
	}

	public @Nullable Group getGroup() {
		return event.getGroupTo()
				.map(name -> LuckPermsProvider.get().getGroupManager().getGroup(name))
				.orElse(null);
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
