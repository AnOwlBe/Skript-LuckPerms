package owlbe.skriptLuckPerms;

import org.bukkit.plugin.java.JavaPlugin;
import ch.njol.skript.Skript;
import org.skriptlang.skript.addon.SkriptAddon;
import owlbe.skriptLuckPerms.modules.Modules;
import owlbe.skriptLuckPerms.luckpermsstuff.RegisterLuckPermEvents;

public final class SkriptLuckPerms extends JavaPlugin {
    public static SkriptLuckPerms instance;
    int pluginId = 31087;
    @Override
    public void onEnable() {
        /* Metrics metrics = */new Metrics(this, pluginId);
        instance = this;
        SkriptAddon addon = Skript.instance().registerAddon(SkriptLuckPerms.class, "skript-luckperms");
        addon.loadModules(new Modules());
        new RegisterLuckPermEvents().register();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}