package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class OnUserMetaRemove extends PlayerEvent {

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private final Node node;

	public OnUserMetaRemove(Player player, Node node) {
		super(player);
		this.node = node;
	}

	public @NotNull String getKey() {;
		if (node.getType() == NodeType.PREFIX)
			return "prefix";
		if (node.getType() == NodeType.SUFFIX)
			return "suffix";
		return ((MetaNode) node).getMetaKey();
	}

	public @NotNull String getValue() {
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
