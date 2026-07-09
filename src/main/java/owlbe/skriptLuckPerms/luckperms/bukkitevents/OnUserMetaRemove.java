package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NonNull;

public class OnUserMetaRemove extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Node event;

    public OnUserMetaRemove(Player player, Node event) {
        super(player);
        this.event = event;

    }

    public String getKey() {
        if (event.getType() == NodeType.PREFIX) return "prefix";
        if (event.getType() == NodeType.SUFFIX) return "suffix";
        return ((MetaNode) event).getMetaKey();
    }
    public String getValue() {
        if (event.getType() == NodeType.PREFIX) return ((PrefixNode) event).getMetaValue();
        if (event.getType() == NodeType.SUFFIX) return ((SuffixNode) event).getMetaValue();
        return ((MetaNode) event).getMetaValue();
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
