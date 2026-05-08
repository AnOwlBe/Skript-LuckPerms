package owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
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
        NodeType<?> type = node.getType();
        if (type == NodeType.PREFIX) return "prefix";
        if (type == NodeType.SUFFIX) return "suffix";
        return ((MetaNode) node).getMetaKey();
    }

    public String getValue() {
        NodeType<?> type = node.getType();
        if (type == NodeType.PREFIX) return ((PrefixNode) node).getMetaValue();
        if (type == NodeType.SUFFIX) return ((SuffixNode) node).getMetaValue();
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




