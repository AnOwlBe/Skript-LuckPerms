package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NonNull;

public class OnUserReceiveGroup extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final NodeAddEvent event;


    public OnUserReceiveGroup(Player player,NodeAddEvent event) {
        super(player);
        this.event = event;

    }
    public Timespan getDuration() {
        return new Timespan(event.getNode().getExpiry() != null ? event.getNode().getExpiry().toEpochMilli() - System.currentTimeMillis() : 0);
    }
    public Group getGroup() {
        String group = ((InheritanceNode) event.getNode()).getGroupName();
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



