package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.track.mutate.TrackRemoveGroupEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;

public class TrackRemoveGroupListener {

	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(SkriptLuckPerms.getPluginInstance(), TrackRemoveGroupEvent.class, event -> {
			bukkitScheduler.runTask(SkriptLuckPerms.getPluginInstance(), () -> {
			//	pluginManager.callEvent(new OnTrackRemoveGroup(event));
			});
		});
	}

}
