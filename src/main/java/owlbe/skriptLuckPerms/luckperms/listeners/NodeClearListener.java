package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeClearEvent;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.GroupRemoveEvent;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.MetaRemoveEvent;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.PermissionRemoveEvent;

import static net.luckperms.api.node.NodeType.PERMISSION;
import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class NodeClearListener {

	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, NodeClearEvent.class, event -> {
			for (Node node : event.getNodes()) {
				NodeType<?> type = node.getType();

				if (type == NodeType.INHERITANCE) {
					if (node instanceof InheritanceNode inheritanceNode)
						pluginManager.callEvent(new GroupRemoveEvent(event.getTarget(), inheritanceNode));
				}

				if (type == NodeType.PREFIX || type == NodeType.SUFFIX || type == NodeType.META)
					bukkitScheduler.runTask(instance, () -> pluginManager.callEvent(new MetaRemoveEvent(event, node)));

				if (type == PERMISSION)
					bukkitScheduler.runTask(instance, () -> pluginManager.callEvent(new PermissionRemoveEvent(event, node)));
			}
		});
	}

}
