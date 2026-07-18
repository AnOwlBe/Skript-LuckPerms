package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class OnGroupLosePermission extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final NodeRemoveEvent event;

	public OnGroupLosePermission(NodeRemoveEvent event) {
		this.event = event;
	}

	public @Nullable Group getGroup() {
		String groupName = ((InheritanceNode) event.getNode()).getGroupName();
		return LuckPermsProvider.get().getGroupManager().getGroup(groupName);
	}

	public PermissionNode getPermission() {
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
