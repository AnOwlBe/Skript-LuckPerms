package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.*;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class NodeRemoveListener {

	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, NodeRemoveEvent.class, event -> {
			NodeType<?> type = event.getNode().getType();
			boolean isUser = event.isUser();
			boolean isGroup = event.isGroup();
			bukkitScheduler.runTask(instance, () -> {
				if (isUser && type == NodeType.PERMISSION) {
					User user = (User) event.getTarget();
					pluginManager.callEvent(new OnUserLosePermission(Bukkit.getPlayer(user.getUniqueId()), event));
				}
				if (isGroup && type == NodeType.PERMISSION) {
					pluginManager.callEvent(new OnGroupLosePermission(event));
				}
				if (isUser && type == NodeType.INHERITANCE) {
					User user = (User) event.getTarget();
					Player player = Bukkit.getPlayer(user.getUniqueId());
					pluginManager.callEvent(new OnUserLoseGroup(player, event));
				}
				if (isGroup && (type == NodeType.PREFIX || type == NodeType.SUFFIX || type == NodeType.META)) {
					pluginManager.callEvent(new OnGroupMetaRemove((Group) event.getTarget(), event.getNode()));
				}
				if (isUser && (type == NodeType.PREFIX || type == NodeType.SUFFIX || type == NodeType.META)) {
					User user = (User) event.getTarget();
					Player player = Bukkit.getPlayer(user.getUniqueId());
					pluginManager.callEvent(new OnUserMetaRemove(player, event.getNode()));
				}
			});
		});
	}

}
