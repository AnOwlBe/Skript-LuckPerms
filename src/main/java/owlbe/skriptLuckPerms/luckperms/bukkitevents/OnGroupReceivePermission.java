package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

public class OnGroupReceivePermission extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final NodeAddEvent event;

	public OnGroupReceivePermission(NodeAddEvent event) {
		this.event = event;
	}

	public Group getGroup() {
		return (Group) event.getTarget();
	}

	public PermissionNode getPermission() {
		return (PermissionNode) event.getNode();
	}

	public Timespan getDuration() {
		Duration expiry = event.getNode().getExpiryDuration();
		if (expiry == null)
			return new Timespan(0);
		return new Timespan(expiry.toMillis());
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NonNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
