package owlbe.skriptLuckPerms;

import ch.njol.skript.Skript;
import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.localization.Localizer;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.listeners.LuckPermsListeners;
import owlbe.skriptLuckPerms.modules.Modules;
import owlbe.skriptLuckPerms.skript.properties.Properties;
import owlbe.skriptLuckPerms.update.UpdateChecker;
import owlbe.skriptLuckPerms.utils.ConfigUpdater;
import owlbe.skriptLuckPerms.utils.Logger;
import owlbe.skriptLuckPerms.utils.SyntaxUtils;

public final class SkriptLuckPerms extends JavaPlugin {

	private static SkriptLuckPerms instance;
	private static SkriptAddon addon;

	@Override
	public void onEnable() {
		//<editor-fold desc="on enable" defaultstate="collapsed">
		Logger.fine("Enabling Skript-LuckPerms..");

		int pluginId = 31087;
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

		SyntaxRegistry syntaxRegistry = addon.syntaxRegistry();

		Properties.register(syntaxRegistry);
		addon.loadModules(new Modules());

		Localizer addonLocalizer = addon.localizer();
		addonLocalizer.setSourceDirectories("lang", null);

		Logger.fine("Loaded " + SyntaxUtils.getTotalAmount(addon) + " elements:");

		long propertyCount = SyntaxUtils.getPropertyAmount(addon);

		Logger.fine(" - 14 Types"); // Until Skript implements a registry for ClassInfo
		for (SyntaxRegistry.Key<? extends SyntaxInfo<?>> syntaxKey : SyntaxUtils.SYNTAX_KEYS) {
			long amount = SyntaxUtils.getAmount(addon, syntaxKey);
			Logger.fine(" - " + amount + " " + SyntaxUtils.formatKeyName(syntaxKey, amount));
		}

		Logger.fine(" - " + propertyCount + " " + (propertyCount == 1 ? "Property" : "Properties"));

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
