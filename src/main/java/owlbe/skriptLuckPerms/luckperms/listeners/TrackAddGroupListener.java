package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.track.mutate.TrackAddGroupEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;

public class TrackAddGroupListener {

	public static void register(EventBus eventBus, BukkitScheduler bukkitScheduler, PluginManager pluginManager) {
		eventBus.subscribe(SkriptLuckPerms.getPluginInstance(), TrackAddGroupEvent.class, event -> {
			bukkitScheduler.runTask(SkriptLuckPerms.getPluginInstance(), () -> {
			//	pluginManager.callEvent(new OnTrackAddGroup(event));
			});
		});
	}

}
