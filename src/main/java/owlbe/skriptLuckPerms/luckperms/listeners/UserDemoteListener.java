package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnUserDemote;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class UserDemoteListener {
	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, UserDemoteEvent.class, event -> {
			bukkitScheduler.runTask(instance, () -> {
				pluginManager.callEvent(new OnUserDemote(Bukkit.getPlayer(event.getUser().getUniqueId()), event));
			});
		});
	}
}
