package owlbe.skriptLuckPerms.luckperms.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.EventBus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.*;
public class Register {

    public static void register() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        EventBus eventBus = luckPerms.getEventBus();
        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
        PluginManager pluginManager=  Bukkit.getPluginManager();

        NodeAddListener.register(eventBus, bukkitScheduler, pluginManager);
        NodeRemoveListener.register(eventBus, bukkitScheduler, pluginManager);
        NodeClearListener.register(eventBus, bukkitScheduler, pluginManager);
        TrackAddGroupListener.register(eventBus, bukkitScheduler, pluginManager);
        TrackRemoveGroupListener.register(eventBus, bukkitScheduler, pluginManager);
        UserDemoteListener.register(eventBus, bukkitScheduler, pluginManager);
        UserPromoteListener.register(eventBus, bukkitScheduler, pluginManager);

    }

}
