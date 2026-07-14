package owlbe.skriptLuckPerms.modules.users.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.OnUserReceivePermission;

public class EvtUserReceivePermission extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtUserReceivePermission.class, "User Receive Permission")
				.supplier(EvtUserReceivePermission::new)
				.addEvent(OnUserReceivePermission.class)
				.addPattern("[luckperm[s]] user receive perm[ission]")
				.addDescription("""
				Called when a user receives a permission.
				If the permission duration is infinite %event-timespan% will return 0 seconds.
				
				`event-permission` = The permission that the user received.
				`event-timespan` = The duration the user will have the permission for.
				""")
				.addExample("""
						on user receive permission:
							send "You just got the permission %event-permission% for %event-timespan%" to player
						""")
				.addSince("1.0")
				.build());

		eventValueRegistry.register(EventValue.builder(OnUserReceivePermission.class, PermissionNode.class)
				.getter(OnUserReceivePermission::getPermission)
				.patterns("permission")
				.build());

		eventValueRegistry.register(EventValue.builder(OnUserReceivePermission.class, Timespan.class)
				.getter(OnUserReceivePermission::getDuration)
				.patterns("timespan")
				.build());
	}

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean check(Event event) {
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "user receive permission";
	}

}
