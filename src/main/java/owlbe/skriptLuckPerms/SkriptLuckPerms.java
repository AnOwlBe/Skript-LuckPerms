package owlbe.skriptLuckPerms;

import org.bukkit.plugin.java.JavaPlugin;
import ch.njol.skript.Skript;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.localization.Localizer;
import owlbe.skriptLuckPerms.modules.Modules;
import owlbe.skriptLuckPerms.luckpermsstuff.RegisterLuckPermEvents;
import owlbe.skriptLuckPerms.update.UpdateChecker;

import java.io.File;

public final class SkriptLuckPerms extends JavaPlugin {
    public static SkriptLuckPerms instance;
    int pluginId = 31087;

    @Override
    public void onEnable() {
        new Metrics(this, pluginId);
        instance = this;
        if (!(new File(instance.getDataFolder(), "config.yml").exists())) {
            saveResource("config.yml", false);
        }
        SkriptAddon addon = Skript.instance().registerAddon(SkriptLuckPerms.class, "skript-luckperms");
        addon.loadModules(new Modules());
        Localizer addonLocalizer = addon.localizer();
        addonLocalizer.setSourceDirectories("lang", null);
        new RegisterLuckPermEvents().register();
        UpdateChecker.enable();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
