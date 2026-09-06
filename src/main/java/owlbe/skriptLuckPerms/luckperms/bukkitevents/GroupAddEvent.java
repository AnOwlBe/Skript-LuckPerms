package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.parent.PermissionHolderEvent;

public class GroupAddEvent extends PermissionHolderEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();

	public GroupAddEvent(PermissionHolder holder, InheritanceNode node) {
		super(holder, node);
	}


	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
