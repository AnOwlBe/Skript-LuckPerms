package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.parent.PermissionHolderEvent;

public class PermissionAddEvent extends PermissionHolderEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final NodeAddEvent event;

	public PermissionAddEvent(NodeAddEvent event) {
		super(event.getTarget(), event.getNode());
		this.event = event;
	}

	public @Nullable PermissionNode getPermission() {
		// in theory, it should always be a PermissionNode but to be safe
		if (event.getNode() instanceof PermissionNode permissionNode)
			return permissionNode;
		return null;
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
