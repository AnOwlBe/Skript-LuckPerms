package owlbe.skriptLuckPerms.luckpermsstuff;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeClearEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckpermsstuff.luckpermsevents.*;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class RegisterLuckPermEvents {
    public void register() {
        LuckPerms lp = LuckPermsProvider.get();
        EventBus eventBus = lp.getEventBus();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        PluginManager manager = Bukkit.getPluginManager();
        // make stuff use scheduler var instead at some point
        // same for manager
        eventBus.subscribe(instance, UserPromoteEvent.class, event -> {
            scheduler.runTask(instance, () ->
                    manager.callEvent(new OnUserPromote(Bukkit.getPlayer(event.getUser().getUniqueId()), event)));

        });
        eventBus.subscribe(instance, UserDemoteEvent.class, event -> {
            scheduler.runTask(instance, () -> {
                manager.callEvent(new OnUserDemote(Bukkit.getPlayer(event.getUser().getUniqueId()), event));
            });
        });
        eventBus.subscribe(instance, NodeAddEvent.class, event -> {
            NodeType<?> type = event.getNode().getType();
            boolean isUser = event.isUser();
            Boolean isGroup = event.isGroup();
            // At some point make all of this just use node instead of the whole event.getNode().getType()
            scheduler.runTask(instance, () -> {
                if (isUser && type == NodeType.PERMISSION) {
                    User user = (User) event.getTarget();
                    manager.callEvent(new OnUserReceivePermission(Bukkit.getPlayer(user.getUniqueId()), event));
                }
                if (event.isGroup() && type == NodeType.PERMISSION) {
                    manager.callEvent(new OnGroupReceivePermission(event));
                }
                if (event.isUser() && type == NodeType.INHERITANCE) {
                    User user = (User) event.getTarget();
                    Player player = Bukkit.getPlayer(user.getUniqueId());
                    manager.callEvent(new OnUserReceiveGroup(player, event));
                }

                if (event.isUser() && (type == NodeType.META || type == NodeType.PREFIX || type == NodeType.SUFFIX)) {
                    User user = (User) event.getTarget();
                    Player player = Bukkit.getPlayer(user.getUniqueId());
                    manager.callEvent(new OnUserMetaSet(player, event));
                }
                if (event.isGroup() && (type == NodeType.META || type == NodeType.PREFIX || type == NodeType.SUFFIX)) {
                    manager.callEvent(new OnGroupMetaSet(event));
                }
            });
        });
        eventBus.subscribe(instance, NodeRemoveEvent.class, event -> {
            NodeType<?> type = event.getNode().getType();
            scheduler.runTask(instance, () -> {
                if (event.isUser() && type == NodeType.PERMISSION) {
                    User user = (User) event.getTarget();
                    manager.callEvent(new OnUserLosePermission(Bukkit.getPlayer(user.getUniqueId()), event));
                }
                if (event.isGroup() && type == NodeType.PERMISSION) {
                    manager.callEvent(new OnGroupLosePermission(event));
                }
                if (event.isUser() && type == NodeType.INHERITANCE) {
                    User user = (User) event.getTarget();
                    Player player = Bukkit.getPlayer(user.getUniqueId());
                    manager.callEvent(new OnUserLoseGroup(player, event));
                }
                if (event.isGroup() && (type == NodeType.PREFIX || type == NodeType.SUFFIX || type == NodeType.META)) {
                    manager.callEvent(new OnGroupMetaRemove((Group) event.getTarget(),event.getNode()));
                }
                if (event.isUser() && (type == NodeType.PREFIX || type == NodeType.SUFFIX || type == NodeType.META)) {
                    User user = (User) event.getTarget();
                    Player player = Bukkit.getPlayer(user.getUniqueId());
                    manager.callEvent(new OnUserMetaRemove(player,event.getNode()));
                }
            });
        });
        eventBus.subscribe(instance, NodeClearEvent.class, event -> {
            for (Node node : event.getNodes()) {
                NodeType<?> type = node.getType();
                if (type != NodeType.PREFIX && type != NodeType.SUFFIX && type != NodeType.META) continue;
                scheduler.runTask(instance, () -> {
                    if (event.isUser()) {
                        User user = (User) event.getTarget();
                        Player player = Bukkit.getPlayer(user.getUniqueId());
                        manager.callEvent(new OnUserMetaRemove(player, node));
                    } else if (event.isGroup()) {
                        manager.callEvent(new OnGroupMetaRemove((Group) event.getTarget(),node));
                    }
                });
            }
        });
    }
}




