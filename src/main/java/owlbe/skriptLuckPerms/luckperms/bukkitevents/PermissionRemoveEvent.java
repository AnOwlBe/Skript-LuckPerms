package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.parent.PermissionHolderEvent;

public class PermissionRemoveEvent extends PermissionHolderEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Node node;

	public PermissionRemoveEvent(NodeMutateEvent event, Node node) {
		super(event.getTarget(), node);
		this.node = node;
	}

	public @Nullable PermissionNode getPermission() {
		// in theory, it should always be a PermissionNode but to be safe
		if (node instanceof PermissionNode permissionNode)
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
