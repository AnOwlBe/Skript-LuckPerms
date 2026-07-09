package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeClearEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnGroupMetaRemove;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnUserMetaRemove;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class NodeClearListener {
	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, NodeClearEvent.class, event -> {
			for (Node node : event.getNodes()) {
				NodeType<?> type = node.getType();
				if (type != NodeType.PREFIX && type != NodeType.SUFFIX && type != NodeType.META) continue;
				bukkitScheduler.runTask(instance, () -> {
					if (event.isUser()) {
						User user = (User) event.getTarget();
						Player player = Bukkit.getPlayer(user.getUniqueId());
						pluginManager.callEvent(new OnUserMetaRemove(player, node));
					} else if (event.isGroup()) {
						pluginManager.callEvent(new OnGroupMetaRemove((Group) event.getTarget(), node));
					}
				});
			}
		});
	}
}
