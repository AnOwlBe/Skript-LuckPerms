package owlbe.skriptLuckPerms.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.ApiStatus;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

// TODO: This could probably be more advanced in future so it  doesn't need to be hard coded?
public final class ConfigUpdater {

	private ConfigUpdater() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	/**
	 * A utility class to automatically update from the old config while preserving config options.
	 */
	@ApiStatus.Internal
	public static void update() {
		Logger.fine("Outdated configuration file detected. Refactoring..");
		FileConfiguration config = instance.getConfig();

		boolean enabled = config.getBoolean("check-for-updates");
		boolean checkAsync = config.getBoolean("check-async-for-updates");

		instance.saveResource("config.yml", true);
		instance.reloadConfig();

		config = instance.getConfig();
		config.set("updatechecker.enabled", enabled);
		config.set("updatechecker.type", checkAsync ? "ASYNC" : "SYNC");

		instance.saveConfig();
		Logger.fine("Outdated configuration refactor complete.");
	}
}
