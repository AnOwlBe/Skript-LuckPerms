package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

public class OnGroupMetaRemove extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Group group;
	private final Node node;

	public OnGroupMetaRemove(Group group, Node node) {
		this.group = group;
		this.node = node;
	}

	public Group getGroup() {
		return group;
	}

	public String getKey() {
		if (node.getType() == NodeType.PREFIX)
			return "prefix";
		if (node.getType() == NodeType.SUFFIX)
			return "suffix";
		return ((MetaNode) node).getMetaKey();
	}

	public String getValue() {
		return ((MetaNode) node).getMetaValue();
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NonNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
