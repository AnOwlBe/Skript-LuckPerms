package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.parent.PermissionHolderEvent;

public class GroupRemoveEvent extends PermissionHolderEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final PermissionHolder holder;
	private final InheritanceNode node;

	public GroupRemoveEvent(PermissionHolder holder, InheritanceNode node) {
		super(holder, node);
		this.holder = holder;
		this.node = node;
	}

	public @NotNull InheritanceNode getNode() {
		return this.node;
	}

	public @NotNull PermissionHolder getTarget() {
		return this.holder;
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
