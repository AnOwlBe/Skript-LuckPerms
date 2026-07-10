package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.event.node.NodeRemoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NonNull;

public class OnUserLosePermission extends PlayerEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final NodeRemoveEvent event;

	public OnUserLosePermission(Player player, NodeRemoveEvent event) {
		super(player);
		this.event = event;
	}

	public String getPermission() {
		return event.getNode().getKey();
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NonNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
