package owlbe.skriptLuckPerms.update;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;
import static owlbe.skriptLuckPerms.utils.MiniMessageUtils.minimessage;

// Credit to
// https://github.com/ShaneBeee/SkBee/tree/master/src/main/java/com/shanebeestudios/skbee/api/util/update for the original versions
// & https://github.com/3add/PacketEventsSK/tree/main/src/main/java/dev/threeadd/packeteventssk/update for the version that this is build upon

public class JoinListener implements Listener {

	@EventHandler
	private void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("skriptluckperms.updates.view"))
			return;

		Bukkit.getScheduler().runTaskLater(instance, () ->
				UpdateChecker.getUpdateVersion(true).thenAccept(version -> {
					player.sendMessage(minimessage("<dark_gray>[<shadow:#63FFA4:0.3><gradient:#63FFA4:#9CFFC5:#63FFA4><b>SKRIPT-LUCKPERMS<reset><dark_gray>] <reset><white>There is a newer version of Skript-LuckPerms:"));
					player.sendMessage(minimessage("<shadow:#63FFA4:0.3><#63FFA4>⚑ <reset><white>Version: <shadow:#63FFA4:0.3><#63FFA4>" + version.getUpdateVersion()));
					player.sendMessage(minimessage("<shadow:#63FFA4:0.3><#63FFA4>⏩ <reset><white>Download at: <shadow:#63FFA4:0.3><#63FFA4><click:open_url:" + version.getUpdateLink() + ">" + version.getUpdateLink()));
				}), 30L);
	}

}
