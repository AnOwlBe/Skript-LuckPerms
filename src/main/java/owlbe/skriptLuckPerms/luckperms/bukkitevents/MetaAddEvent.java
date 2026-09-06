package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.event.node.NodeAddEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.parent.PermissionHolderEvent;

public class MetaAddEvent extends PermissionHolderEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();

	public MetaAddEvent(NodeAddEvent event) {
		super(event.getTarget(), event.getNode());
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
