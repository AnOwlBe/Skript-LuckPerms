package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.UserDemoteEvent;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class UserDemoteListener {

	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, net.luckperms.api.event.user.track.UserDemoteEvent.class, event -> {
			bukkitScheduler.runTask(instance, () -> {
				pluginManager.callEvent(new UserDemoteEvent(Bukkit.getPlayer(event.getUser().getUniqueId()), event));
			});
		});
	}

}
