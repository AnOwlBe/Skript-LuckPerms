package owlbe.skriptLuckPerms.update;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.bukkit.Bukkit;
import owlbe.skriptLuckPerms.utilitities.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

// Credit to
// https://github.com/ShaneBeee/SkBee/tree/master/src/main/java/com/shanebeestudios/skbee/api/util/update for the original versions
// & https://github.com/3add/PacketEventsSK/tree/main/src/main/java/dev/threeadd/packeteventssk/update for the version that this is build upon

public class UpdateChecker {

	private static final Version SERVER_VERSION = Skript.getMinecraftVersion();
	private static Version PLUGIN_VERSION;
	private static ModrinthVersion CURRENT_UPDATE_VERSION;

	public static void enable() {
		PLUGIN_VERSION = new Version(instance.getPluginMeta().getVersion());

		if (instance.getConfig().getBoolean("check-for-updates")) {
			Bukkit.getPluginManager().registerEvents(new JoinListener(), instance);
			boolean executeAsync = instance.getConfig().getBoolean("check-async-for-updates");
			checkUpdate(executeAsync);
		}
	}

	private static void checkUpdate(boolean async) {
		instance.getLogger().fine("Checking for updates..");
		getUpdateVersion(async).thenApply(modrinthVersion -> {
			if (modrinthVersion != null) {
				Logger.warning("Plugin is not up to date!");
				Logger.warning("<white>Current version: <#FF3C47>v" + PLUGIN_VERSION);
				Logger.warning("<white>Available update: <#3CFF6E>v" + modrinthVersion.getUpdateVersion());
				if (modrinthVersion.isServerSupported(SERVER_VERSION)) {
					Logger.warning("<white>Download at: <#3CFF6E>" + modrinthVersion.getUpdateLink());
				} else {
					Logger.warning("<white>Your server version (" + SERVER_VERSION + ") does not support this update.");
					Logger.warning("<white>Supported Versions:");
					for (Version supportedVersion : modrinthVersion.getSupportedVersions()) {
						Logger.warning("  - " + supportedVersion.toString());
					}
				}
			}
			return true;
		}).exceptionally(ignored -> {
			Logger.fine("<#3CFF6E>Plugin is up to date!");
			return true;
		});
	}

	protected static CompletableFuture<ModrinthVersion> getUpdateVersion(boolean async) {
		CompletableFuture<ModrinthVersion> updateVersionFuture = new CompletableFuture<>();
		if (CURRENT_UPDATE_VERSION != null) {
			updateVersionFuture.complete(CURRENT_UPDATE_VERSION);
		} else {
			CompletableFuture<ModrinthVersion> latestReleaseFuture = new CompletableFuture<>();
			if (async) {
				Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
					ModrinthVersion latest = getLatestVersionFromModrinth();
					if (latest == null) {
						latestReleaseFuture.cancel(true);
					} else {
						latestReleaseFuture.complete(latest);
					}
				});
			} else {
				ModrinthVersion latest = getLatestVersionFromModrinth();
				if (latest == null) {
					latestReleaseFuture.cancel(true);
				} else {
					latestReleaseFuture.complete(latest);
				}
			}
			latestReleaseFuture.thenApply(version -> {
				if (version.getUpdateVersion().compareTo(PLUGIN_VERSION) <= 0) {
					updateVersionFuture.cancel(true);
				} else {
					CURRENT_UPDATE_VERSION = version;
					updateVersionFuture.complete(CURRENT_UPDATE_VERSION);
				}
				return true;
			});
		}
		return updateVersionFuture;
	}

	private static ModrinthVersion getLatestVersionFromModrinth() {
		try {
			URL url = new URI("https://api.modrinth.com/v2/project/V2b5AoZA/version").toURL();
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			JsonArray elements = new Gson().fromJson(reader, JsonArray.class);
			JsonElement latestVersion = elements.get(0);
			return new ModrinthVersion(latestVersion);
		} catch (IOException | URISyntaxException e) {
			Logger.warning("<#FF4040>Checking for updates failed!");
		}
		return null;
	}

}
