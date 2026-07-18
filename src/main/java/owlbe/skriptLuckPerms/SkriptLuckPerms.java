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
import owlbe.skriptLuckPerms.utilitities.ConfigUpdater;

import static owlbe.skriptLuckPerms.utilitities.MiniMessageUtils.minimessage;

public final class SkriptLuckPerms extends JavaPlugin {

	public static SkriptLuckPerms instance;
	public static SkriptAddon addon;
	int pluginId = 31087;

	@Override
	public void onEnable() {
		new Metrics(this, pluginId);
		instance = this;
		saveDefaultConfig();
		if (getConfig().isSet("check-for-updates"))
			ConfigUpdater.update();
		addon = Skript.instance().registerAddon(SkriptLuckPerms.class, "skript-luckperms");
		Properties.register(addon.syntaxRegistry());
		addon.loadModules(new Modules());
		Localizer addonLocalizer = addon.localizer();
		addonLocalizer.setSourceDirectories("lang", null);

		Register.register();
		UpdateChecker.enable();


	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(minimessage("Disabling Skript-LuckPerms.."));
	}

}
