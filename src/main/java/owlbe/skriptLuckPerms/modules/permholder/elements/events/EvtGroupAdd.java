package owlbe.skriptLuckPerms.modules.permholder.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.GroupAddEvent;
import owlbe.skriptLuckPerms.utils.events.Type;

import static owlbe.skriptLuckPerms.utils.events.Type.GROUP;
import static owlbe.skriptLuckPerms.utils.events.Type.USER;

public class EvtGroupAdd extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtGroupAdd.class, "Group Add")
				.supplier(EvtGroupAdd::new)
				.addEvent(GroupAddEvent.class)
				.addPattern("[luckperm[s]] group added [to [luckperm[s]] (:group|:user)]")
				.addDescription("""
				Called when a group is added to a permission holder (a user or group).
				
				Event Values:
				`event-inheritance node` = The inheritance node (the group) the holder received.
				`event-group` = The group that received the group, if the holder was a group.
				`event-offline player` = The player that received the group, if the holder was a user.
				""")
				.addExample("""
						on user receive group:
							send "You just received %event-group% for %event-timespan%!" to player
						""")
				.addSince("1.0")
				.addSince("INSERT VERSION (pattern rewrite)")
				.build());

		eventValueRegistry.register(EventValue.builder(GroupAddEvent.class, InheritanceNode.class)
				.getter(GroupAddEvent::getNode)
				.patterns("inheritance [node]")
				.build());
	}

	private @Nullable Type type;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (parseResult.hasTag("user")) {
			type = USER;
		} else if (parseResult.hasTag("group")) {
			type = GROUP;
		}

		return true;
	}

	@Override
	public boolean check(Event event) {
		GroupAddEvent groupEvent = (GroupAddEvent) event;

		boolean typeMatched = true;
		if (type != null) {
			typeMatched = switch (type) {
				case USER -> groupEvent.getTarget() instanceof User;
				case GROUP -> groupEvent.getTarget() instanceof Group;
			};
		}

		return typeMatched;
	}

	@Override
	@SuppressWarnings("DataFlowIssue") // type.toString will never be null
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("luckperms group")
				.append("added")
				.appendIf(type != null, "to", type.toString())
				.toString();
	}

}
