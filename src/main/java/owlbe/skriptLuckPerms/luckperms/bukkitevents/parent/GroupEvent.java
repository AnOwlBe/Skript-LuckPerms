package owlbe.skriptLuckPerms.luckperms.bukkitevents.parent;

import net.luckperms.api.model.group.Group;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GroupEvent extends Event {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Group group;

	public GroupEvent(Group group) {
		this.group = group;
	}

	public Group getGroup() {
		return this.group;
	}

	public String getGroupName() {
		return this.group.getName();
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

}
