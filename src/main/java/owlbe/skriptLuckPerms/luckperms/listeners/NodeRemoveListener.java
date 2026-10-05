package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.GroupRemoveEvent;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.MetaRemoveEvent;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.PermissionRemoveEvent;

public class NodeRemoveListener {

	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(SkriptLuckPerms.getPluginInstance(), NodeRemoveEvent.class, event -> {
			NodeType<?> type = event.getNode().getType();
			Node node = event.getNode();
			boolean isUser = event.isUser();
			boolean isGroup = event.isGroup();

			bukkitScheduler.runTask(SkriptLuckPerms.getPluginInstance(), () -> {
				if (type == NodeType.PERMISSION)
					pluginManager.callEvent(new PermissionRemoveEvent(event, node));

				if (type == NodeType.INHERITANCE) {
					if (node instanceof InheritanceNode inheritanceNode)
						pluginManager.callEvent(new GroupRemoveEvent(event.getTarget(), inheritanceNode));
				}

				if (type == NodeType.PREFIX || type == NodeType.SUFFIX || type == NodeType.META)
					pluginManager.callEvent(new MetaRemoveEvent(event, node));
			});
		});
	}

}
