package owlbe.skriptLuckPerms.luckperms.bukkitevents.parent;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserEvent extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final OfflinePlayer player;

	public UserEvent(OfflinePlayer player) {
		this.player = player;
	}

	public OfflinePlayer getPlayer() {
		return this.player;
	}

	public OfflinePlayer getOfflinePlayer() {
		return this.player;
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
