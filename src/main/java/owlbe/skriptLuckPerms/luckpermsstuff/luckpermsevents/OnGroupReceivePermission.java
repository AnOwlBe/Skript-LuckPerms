package owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

public class OnGroupReceivePermission extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final NodeAddEvent event;

    public OnGroupReceivePermission(NodeAddEvent event) {
        this.event = event;

    }
    public Group getGroup() {
        return (Group) event.getTarget();
    }
    public String getPermission() {
        return event.getNode().getKey();
    }
    public Timespan getDuration() {
        return new Timespan(event.getNode().getExpiry() != null ? event.getNode().getExpiry().toEpochMilli() - System.currentTimeMillis() : 0);
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}