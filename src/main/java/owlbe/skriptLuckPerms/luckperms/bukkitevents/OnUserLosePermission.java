package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class OnUserLosePermission extends PlayerEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final NodeRemoveEvent event;

	public OnUserLosePermission(Player player, NodeRemoveEvent event) {
		super(player);
		this.event = event;
	}

	public @NotNull PermissionNode getPermission() {
		return (PermissionNode) event.getNode();
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NonNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
