package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class OnGroupMetaSet extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final NodeAddEvent event;

	public OnGroupMetaSet(NodeAddEvent event) {
		this.event = event;
	}

	public @Nullable Group getGroup() {
		String groupName = ((InheritanceNode) event.getNode()).getGroupName();
		return LuckPermsProvider.get().getGroupManager().getGroup(groupName);
	}

	public @NotNull String getKey() {
		Node node = event.getNode();
		if (node.getType() == NodeType.PREFIX)
			return "prefix";
		if (node.getType() == NodeType.SUFFIX)
			return "suffix";
		return ((MetaNode) node).getMetaKey();
	}

	public @NotNull String getValue() {
		return ((MetaNode) event.getNode()).getMetaValue();
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NonNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
