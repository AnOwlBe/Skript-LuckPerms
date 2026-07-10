package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

public class OnGroupMetaSet extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final NodeAddEvent event;

	public OnGroupMetaSet(NodeAddEvent event) {
		this.event = event;
	}

	public Group getGroup() {
		return (Group) event.getTarget();
	}

	public String getKey() {
		Node node = event.getNode();
		if (node.getType() == NodeType.PREFIX)
			return "prefix";
		if (node.getType() == NodeType.SUFFIX)
			return "suffix";
		return ((MetaNode) node).getMetaKey();
	}

	public String getValue() {
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
