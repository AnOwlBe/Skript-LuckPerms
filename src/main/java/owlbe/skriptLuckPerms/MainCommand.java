package owlbe.skriptLuckPerms;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptUpdater;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.node.permission.elements.expressions.ExprSecCreatePermission;
import owlbe.skriptLuckPerms.utils.SyntaxUtils;

import java.util.List;

import static owlbe.skriptLuckPerms.utils.MiniMessageUtils.PREFIX;
import static owlbe.skriptLuckPerms.utils.MiniMessageUtils.sendMessage;

public final class MainCommand {

	private MainCommand() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	public static void register(LifecycleEventManager<Plugin> manager) {
		LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("skript-luckperms")
				.requires(context -> context.getSender().hasPermission("skriptluckperms.command"))
				.then(Commands.literal("info")
						.executes(context -> {
							sendInfo(context.getSource().getSender());
							return Command.SINGLE_SUCCESS;
						}))
				.then(Commands.literal("wiki")
						.executes(context -> {
							CommandSender sender = context.getSource().getSender();
							sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset>" + PREFIX + " <white>Wiki: <shadow:#63FFA4:0.3><#63FFA4><click:open_url:https://skript-luckperms.gitbook.io/skript-luckperms>skript-luckperms.gitbook.io/skript-luckperms");
							return Command.SINGLE_SUCCESS;
						}));

		manager.registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(
				command.build(),
				"Primary command for Skript-LuckPerms",
				List.of("skript-lp", "skriptluckperms", "skriptlp")
				));
	}

	private static void sendInfo(CommandSender sender) {
		sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset>" + PREFIX + " <white>Information<newline>");
		sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset><white>Server Version: " + Bukkit.getName() + " " + Bukkit.getVersion());

		SkriptUpdater updater = Skript.getInstance().getUpdater();
		String flavor = "Unknown Flavor";

		if (updater != null)
			flavor = updater.getCurrentRelease().flavor;

		SkriptAddon addon = SkriptLuckPerms.getAddonInstance();

		sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset><white>Skript Version: " + Skript.getVersion() + " " + flavor);

		String version = SkriptLuckPerms.getPluginInstance().getPluginMeta().getVersion();
		sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset><white>Plugin Version: " + version);

		sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset>There are " + SyntaxUtils.getTotalAmount(addon) + " total loaded elements:");

		long propertyCount = SyntaxUtils.getPropertyAmount(addon);

		sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset>- 14 Types"); // Until skript implements a registry for ClassInfo
		for (SyntaxRegistry.Key<? extends SyntaxInfo<?>> syntaxKey : SyntaxUtils.SYNTAX_KEYS) {
			long amount = SyntaxUtils.getAmount(addon, syntaxKey);
			sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset>- " + amount + " " + SyntaxUtils.formatKeyName(syntaxKey, amount));
		}

		sendMessage(sender, "<shadow:#63FFA4:0.3><#63FFA4><b>| <reset>- " + propertyCount + " " + (propertyCount == 1 ? "Property" : "Properties"));

	}
}
