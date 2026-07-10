package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.*;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class NodeAddListener {

	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, NodeAddEvent.class, event -> {
			NodeType<?> type = event.getNode().getType();
			boolean isUser = event.isUser();
			boolean isGroup = event.isGroup();
			bukkitScheduler.runTask(instance, () -> {
				if (isUser && type == NodeType.PERMISSION) {
					User user = (User) event.getTarget();
					pluginManager.callEvent(new OnUserReceivePermission(Bukkit.getPlayer(user.getUniqueId()), event));
				}
				if (isGroup && type == NodeType.PERMISSION) {
					pluginManager.callEvent(new OnGroupReceivePermission(event));
				}
				if (isUser && type == NodeType.INHERITANCE) {
					User user = (User) event.getTarget();
					Player player = Bukkit.getPlayer(user.getUniqueId());
					pluginManager.callEvent(new OnUserReceiveGroup(player, event));
				}

				if (isUser && (type == NodeType.META || type == NodeType.PREFIX || type == NodeType.SUFFIX)) {
					User user = (User) event.getTarget();
					Player player = Bukkit.getPlayer(user.getUniqueId());
					pluginManager.callEvent(new OnUserMetaSet(player, event));
				}
				if (isGroup && (type == NodeType.META || type == NodeType.PREFIX || type == NodeType.SUFFIX)) {
					pluginManager.callEvent(new OnGroupMetaSet(event));
				}
			});
		});
	}

}
