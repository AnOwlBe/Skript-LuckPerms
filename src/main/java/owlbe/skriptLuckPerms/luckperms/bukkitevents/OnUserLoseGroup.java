package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NonNull;

public class OnUserLoseGroup extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final NodeRemoveEvent event;

    public OnUserLoseGroup(Player player, NodeRemoveEvent event) {
        super(player);
        this.event = event;
    }

    public Group getGroup() {
        String group =  ((InheritanceNode) event.getNode()).getGroupName();
        return LuckPermsProvider.get().getGroupManager().getGroup(group);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
