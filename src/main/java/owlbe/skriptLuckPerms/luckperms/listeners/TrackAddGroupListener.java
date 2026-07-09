package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.track.mutate.TrackAddGroupEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnTrackAddGroup;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public class TrackAddGroupListener {
	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(instance, TrackAddGroupEvent.class, event -> {
			bukkitScheduler.runTask(instance, () -> {
				pluginManager.callEvent(new OnTrackAddGroup(event));
			});
		});
	}
}
