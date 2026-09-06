package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.GroupAddEvent;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.MetaAddEvent;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.PermissionAddEvent;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class NodeAddListener {

	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, NodeAddEvent.class, event -> {
			NodeType<?> type = event.getNode().getType();
			Node node = event.getNode();
			bukkitScheduler.runTask(instance, () -> {
				if (type == NodeType.PERMISSION)
					pluginManager.callEvent(new PermissionAddEvent(event));

				if (type == NodeType.INHERITANCE) {
					if (node instanceof InheritanceNode inheritanceNode)
						pluginManager.callEvent(new GroupAddEvent(event.getTarget(), inheritanceNode));
				}

				if (type == NodeType.META || type == NodeType.PREFIX || type == NodeType.SUFFIX)
					pluginManager.callEvent(new MetaAddEvent(event));
			});
		});
	}

}
