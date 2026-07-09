package owlbe.skriptLuckPerms;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ch.njol.skript.Skript;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.localization.Localizer;
import owlbe.skriptLuckPerms.modules.Modules;
import owlbe.skriptLuckPerms.luckperms.listeners.Register;
import owlbe.skriptLuckPerms.skript.properties.Properties;
import owlbe.skriptLuckPerms.update.UpdateChecker;

import static owlbe.skriptLuckPerms.utilitities.MiniMessageUtil.minimessage;

public final class SkriptLuckPerms extends JavaPlugin {

    public static SkriptLuckPerms instance;
    public static SkriptAddon addon;
    int pluginId = 31087;

    @Override
    public void onEnable() {
        new Metrics(this, pluginId);
        instance = this;
        saveDefaultConfig();

        addon = Skript.instance().registerAddon(SkriptLuckPerms.class, "skript-luckperms");
        Properties.register(addon.syntaxRegistry());
        addon.loadModules(new Modules());
        Localizer addonLocalizer = addon.localizer();
        addonLocalizer.setSourceDirectories("lang", null);

		Register.register();
        UpdateChecker.enable();
    }

    public static void error(String message) {
        Bukkit.getConsoleSender().sendMessage(minimessage("<#3CFF6E>Skript-LuckPerms <reset><dark_gray>→ <#FF4A59>" + message));
    }

    @Override
    public void onDisable() {
        // null
    }

}
