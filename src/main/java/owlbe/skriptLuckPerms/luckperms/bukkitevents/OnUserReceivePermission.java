package owlbe.skriptLuckPerms.luckperms.bukkitevents;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.event.node.NodeAddEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jspecify.annotations.NonNull;

public class OnUserReceivePermission extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final NodeAddEvent event;

    public OnUserReceivePermission(Player player,NodeAddEvent event) {
        super(player);
        this.event = event;

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


