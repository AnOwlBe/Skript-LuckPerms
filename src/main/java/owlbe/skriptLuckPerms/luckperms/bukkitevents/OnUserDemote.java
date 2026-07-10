package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NonNull;

public class OnUserDemote extends PlayerEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final UserDemoteEvent event;

	public OnUserDemote(Player player,UserDemoteEvent event) {
		super(player);
		this.event = event;
	}

	public String getTrack() {
		return event.getTrack().getName();
	}

	public Group getPreviousGroup() {
		if (event.getGroupFrom().orElse(null) == null) return null;
		return LuckPermsProvider.get().getGroupManager().getGroup(event.getGroupFrom().orElse(null));
	}

	public Group getGroup() {
		if (event.getGroupTo().orElse(null) == null) return null;
		return LuckPermsProvider.get().getGroupManager().getGroup(event.getGroupTo().orElse(null));
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NonNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
