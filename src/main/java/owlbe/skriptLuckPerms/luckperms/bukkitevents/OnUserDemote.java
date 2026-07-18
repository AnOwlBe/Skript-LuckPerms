package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.track.Track;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class OnUserDemote extends PlayerEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final UserDemoteEvent event;

	public OnUserDemote(Player player, UserDemoteEvent event) {
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
	public @NonNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
