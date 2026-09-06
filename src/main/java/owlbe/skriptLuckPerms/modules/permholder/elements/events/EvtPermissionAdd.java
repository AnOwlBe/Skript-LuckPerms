package owlbe.skriptLuckPerms.modules.permholder.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.PermissionAddEvent;
import owlbe.skriptLuckPerms.utils.events.Type;

import static owlbe.skriptLuckPerms.utils.events.Type.GROUP;
import static owlbe.skriptLuckPerms.utils.events.Type.USER;

public class EvtPermissionAdd extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry,EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtPermissionAdd.class, "Permission Add")
				.supplier(EvtPermissionAdd::new)
				.addEvent(PermissionAddEvent.class)
				.addPattern("[luckperm[s]] perm[ission] [%-luckpermspermission%] added [to [luckperm[s]] (:group|:user)]")
				.addDescription("""
						Called when a permission is added to a permission holder (a user or group).
						
						Event Values:
						`event-permission` = The permission the holder received.
						`event-group` = The group that received the permission, if the holder was a group.
						`event-offline player` = The player that received the permission, if the holder was a user.
						""")
				.addExample("""
						on permission "nerd" added to luckperms user:
						    if event-offline player is online:
						        send "You're now officially a nerd!" to event-offline player
						""")
				.addExample("""
						on permission "perk.fly" added to luckperms group:
						    event-group is "example"
						    get the members of group "example" and store it in {_players::*}
						    loop {_players::*}:
						        make loop-player fly
						        send "A group you are in received the flight perk!" to loop-player
						""")
				.addSince("1.0, INSERT VERSION (pattern rewrite)")
				.build());

		eventValueRegistry.register(EventValue.builder(PermissionAddEvent.class, PermissionNode.class)
				.getter(PermissionAddEvent::getPermission)
				.patterns("perm[ission]")
				.build());
	}

	private @Nullable Type type;
	private @Nullable Literal<PermissionNode> permission;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (args[0] != null)
			permission = (Literal<PermissionNode>) args[0];

		if (parseResult.hasTag("user")) {
			type = USER;
		} else if (parseResult.hasTag("group")) {
			type = GROUP;
		}

		return true;
	}

	@Override
	public boolean check(Event event) {
		PermissionAddEvent permEvent = (PermissionAddEvent) event;

		boolean typeMatched = true;
		boolean permMatched = true;

		if (type != null) {
			typeMatched = switch (type) {
				case USER -> permEvent.getTarget() instanceof User;
				case GROUP -> permEvent.getTarget() instanceof Group;
			};
		}

		if (permission != null) {
			permMatched = this.permission.check(event, expected -> {
				PermissionNode permission = permEvent.getPermission();
				if (permission == null)
					return false;

				String stringPermission = permission.getPermission();

				return expected.getPermission().equals(stringPermission);
			});
		}

		return typeMatched && permMatched;
	}

	@Override
	@SuppressWarnings("DataFlowIssue") // type.toString will never be null
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("luckperms permission")
				.appendIf(permission != null, permission)
				.append("added")
				.appendIf(type != null, "to", type.toString())
				.toString();
	}

}
