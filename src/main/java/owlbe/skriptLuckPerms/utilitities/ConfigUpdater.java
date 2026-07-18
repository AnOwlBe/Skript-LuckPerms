package owlbe.skriptLuckPerms.utilitities;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.ApiStatus;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

public final class ConfigUpdater {

	/**
	 * A Utility class to automatically update from the old config while preserving config options.
	 */
	@ApiStatus.Internal
	public static void update() {
		Logger.fine("Outdated config file detected. Refactoring..");
		FileConfiguration config = instance.getConfig();
		boolean enabled = config.getBoolean("check-for-updates");
		boolean checkAsync = config.getBoolean("check-async-for-updates");
		instance.saveResource("config.yml", true);
		instance.reloadConfig();
		config = instance.getConfig();
		config.set("updatechecker.enabled", enabled);
		config.set("updatechecker.type", checkAsync ? "ASYNC" : "SYNC");
		instance.saveConfig();
		Logger.fine("Outdated config refactor complete.");
	}
}
