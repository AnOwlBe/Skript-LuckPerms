package owlbe.skriptLuckPerms;

import ch.njol.skript.Skript;
import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.localization.Localizer;
import owlbe.skriptLuckPerms.luckperms.listeners.LuckPermsListeners;
import owlbe.skriptLuckPerms.modules.Modules;
import owlbe.skriptLuckPerms.skript.properties.Properties;
import owlbe.skriptLuckPerms.update.UpdateChecker;
import owlbe.skriptLuckPerms.utils.ConfigUpdater;
import owlbe.skriptLuckPerms.utils.Logger;

public final class SkriptLuckPerms extends JavaPlugin {

	public static SkriptLuckPerms instance;
	public static SkriptAddon addon;
	int pluginId = 31087;

	@Override
	public void onEnable() {
		//<editor-fold desc="on enable" defaultstate="collapsed">
		Logger.fine("Enabling Skript-LuckPerms..");

		new Metrics(this, pluginId);

		instance = this;

		saveDefaultConfig();
		if (getConfig().isSet("check-for-updates"))
			ConfigUpdater.update();

		setupSkript();
		setupLuckPerms();
		setupPaper();

		UpdateChecker.enable();

		Logger.fine("Skript-LuckPerms enabled successfully!");
		//</editor-fold>
	}

	private void setupSkript() {
		//<editor-fold desc="setup skript" defaultstate="collapsed">
		addon = Skript.instance().registerAddon(SkriptLuckPerms.class, "skript-luckperms");

		Properties.register(addon.syntaxRegistry());
		addon.loadModules(new Modules());

		Localizer addonLocalizer = addon.localizer();
		addonLocalizer.setSourceDirectories("lang", null);
		//</editor-fold>
	}

	private void setupLuckPerms() {
		LuckPermsListeners.register();
	}

	private void setupPaper() {
		MainCommand.register(getLifecycleManager());
	}

	public static SkriptAddon getAddonInstance() {
		return addon;
	}

	public static SkriptLuckPerms getPluginInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
		Logger.fine("Disabling Skript-LuckPerms..");
	}

}
