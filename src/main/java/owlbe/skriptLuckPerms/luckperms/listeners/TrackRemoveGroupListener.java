package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.track.mutate.TrackRemoveGroupEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnTrackRemoveGroup;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class TrackRemoveGroupListener {
	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, TrackRemoveGroupEvent.class, event -> {
			bukkitScheduler.runTask(instance, () -> {
				pluginManager.callEvent(new OnTrackRemoveGroup(event));
			});
		});
	}
}
