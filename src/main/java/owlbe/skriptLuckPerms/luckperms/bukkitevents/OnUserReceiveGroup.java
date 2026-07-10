package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

public class OnUserReceiveGroup extends PlayerEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final NodeAddEvent event;

	public OnUserReceiveGroup(Player player,NodeAddEvent event) {
		super(player);
		this.event = event;
	}

	public Timespan getDuration() {
		Duration expiry = event.getNode().getExpiryDuration();
		if (expiry == null)
			return new Timespan(0);
		return new Timespan(expiry.toMillis());
	}

	public Group getGroup() {
		return ((Group) event.getTarget());
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NonNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
