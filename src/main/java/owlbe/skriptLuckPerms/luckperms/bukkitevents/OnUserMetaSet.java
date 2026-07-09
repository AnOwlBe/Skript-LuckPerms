package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NonNull;

public class OnUserMetaSet extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final NodeAddEvent event;

    public OnUserMetaSet(Player player,NodeAddEvent event) {
        super(player);
        this.event = event;

    }

    public String getKey() {
        NodeType<?> type = event.getNode().getType();
        if (type == NodeType.PREFIX) return "prefix";
        if (type == NodeType.SUFFIX) return "suffix";
        return ((MetaNode) event.getNode()).getMetaKey();
    }
    public String getValue() {
        NodeType<?> type = event.getNode().getType();
        if (type == NodeType.PREFIX) return ((PrefixNode) event.getNode()).getMetaValue();
        if (type == NodeType.SUFFIX) return ((SuffixNode) event.getNode()).getMetaValue();
        return ((MetaNode) event.getNode()).getMetaValue();
    }
    public Group getGroup() {
        return (Group) event.getTarget();
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}


