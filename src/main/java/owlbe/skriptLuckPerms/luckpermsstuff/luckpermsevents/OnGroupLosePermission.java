package owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents;

import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

public class OnGroupLosePermission extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final NodeRemoveEvent event;

    public OnGroupLosePermission(NodeRemoveEvent event) {
        this.event = event;

    }
    public Group getGroup() {
        return (Group) event.getTarget();
    }
    public String getPermission() {
        return event.getNode().getKey();
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
