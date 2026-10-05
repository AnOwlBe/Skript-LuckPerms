package owlbe.skriptLuckPerms.luckperms.bukkitevents.parent;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class PermissionHolderEvent extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final PermissionHolder holder;
	private final Node node;

	public PermissionHolderEvent(PermissionHolder holder, Node node) {
		this.holder = holder;
		this.node = node;
	}

	public PermissionHolder getTarget() {
		return this.holder;
	}

	public Node getNode() {
		return this.node;
	}

	public @NotNull Timespan getExpiry() {
		Duration expiry = node.getExpiryDuration();
		if (expiry == null)
			return new Timespan(0);
		return new Timespan(expiry.toMillis());
	}

	public boolean isUser()  {
		return this.holder instanceof User;
	}

	public boolean isGroup()  {
		return this.holder instanceof Group;
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
