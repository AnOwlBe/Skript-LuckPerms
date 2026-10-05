package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.node.Node;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.parent.PermissionHolderEvent;

public class MetaRemoveEvent extends PermissionHolderEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();

	public MetaRemoveEvent(NodeMutateEvent event, Node node) {
		super(event.getTarget(), node);
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
