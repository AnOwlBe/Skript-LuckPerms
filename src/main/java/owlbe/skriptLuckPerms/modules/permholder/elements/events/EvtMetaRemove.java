package owlbe.skriptLuckPerms.modules.permholder.elements.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.MetaAddEvent;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.MetaRemoveEvent;
import owlbe.skriptLuckPerms.utils.events.Type;

import static net.luckperms.api.node.NodeType.*;
import static owlbe.skriptLuckPerms.utils.events.Type.GROUP;
import static owlbe.skriptLuckPerms.utils.events.Type.USER;

// Note that ChatMetaNode does not extend MetaNode but this event still covers both
public class EvtMetaRemove extends SkriptEvent {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtMetaRemove.class, "Meta Add")
				.supplier(EvtMetaRemove::new)
				.addEvent(MetaRemoveEvent.class)
				.addPattern("[luckperm[s]] ([custom] :meta|:prefix|:suffix) removed [from [luckperm[s]] (:group|:user)]")
				.addDescription("""
						Called when a prefix, suffix, or custom meta is removed from a permission holder (a user or group).
						
						Event Values:
						`event-node` = The node the holder lost, holding either a meta or chat meta node.
						`event-chat meta` = The chat meta node the holder lost, if the node was prefix or suffix.
						`event-meta` = The custom meta the holder lost, if the node was custom meta.
						`event-group` = The group that lost the meta, if the holder was a group.
						`event-offline player` = The player that lost the meta, if the holder was a user.
						""")
				.addExample("""
						on luckperms meta added to user:
						    send actionbar "You just received the meta key '%key of event-meta%' with value '%value of event-meta%'" to event-player
						""")
				.addSince("1.0, INSERT VERSION (pattern rewrite)")
				.build());

		eventValueRegistry.register(EventValue.builder(MetaAddEvent.class, MetaNode.class)
				.getter(event -> {
					if (!(event.getNode() instanceof MetaNode metaNode))
						return null;
					return metaNode;
				})
				.patterns("meta [node]")
				.build());

		eventValueRegistry.register(EventValue.builder(MetaAddEvent.class, ChatMetaNode.class)
				.getter(event -> {
					if (!(event.getNode() instanceof ChatMetaNode<?, ?> chatMetaNode))
						return null;
					return chatMetaNode;
				})
				.patterns("(prefix|suffix|chat)[ ]meta [node]")
				.build());
	}

	private @Nullable Type type;
	private NodeType<?> metaType;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		if (parseResult.hasTag("prefix")) {
			metaType = PREFIX;
		} else if (parseResult.hasTag("suffix")) {
			metaType = SUFFIX;
		} else if (parseResult.hasTag("meta")) {
			metaType = META;
		}

		if (parseResult.hasTag("user")) {
			type = USER;
		} else if (parseResult.hasTag("group")) {
			type = GROUP;
		}

		return true;
	}

	@Override
	public boolean check(Event event) {
		MetaRemoveEvent metaEvent = (MetaRemoveEvent) event;

		boolean typeMatched = true;
		boolean metaMatched;

		if (type != null) {
			typeMatched = switch (type) {
				case USER -> metaEvent.getTarget() instanceof User;
				case GROUP -> metaEvent.getTarget() instanceof Group;
			};
		}

		metaMatched = switch (metaEvent.getNode()) {
			case MetaNode ignored -> metaType == META;
			case PrefixNode ignored -> metaType == PREFIX;
			case SuffixNode ignored -> metaType == SUFFIX;
			default -> false;
		};

		return typeMatched && metaMatched;
	}

	@Override
	@SuppressWarnings("DataFlowIssue")
	public String toString(@Nullable Event event, boolean debug) {
		String metaTypeName = "unknown";
		if (metaType == META)
			metaTypeName = "meta";
		if (metaType == PREFIX)
			metaTypeName = "prefix";
		if (metaType == SUFFIX)
			metaTypeName = "suffix";

		return new SyntaxStringBuilder(event, debug)
				.append("luckperms", metaTypeName)
				.append("removed")
				.appendIf(type != null, "from ", type.toString())
				.toString();
	}

}

