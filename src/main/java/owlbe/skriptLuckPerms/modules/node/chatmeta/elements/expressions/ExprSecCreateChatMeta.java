package owlbe.skriptLuckPerms.modules.node.chatmeta.elements.expressions;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import net.luckperms.api.node.ChatMetaType;
import net.luckperms.api.node.types.ChatMetaNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.utils.wrapper.ChatMetaNodeWrapper;

import java.util.List;

@Name("Chat Meta")
@Description("""
		Creates a prefix or suffix with the given values.
		
		See <the ref> for how to add a prefix or suffix to a group or user.
		""")
@Example("""
	command /freeadmin:
	    trigger:
	        set {_prefix} to a new luckperms prefix from value "<red>Admin":
	            set priority of event-chat meta to 900
	            set expiry of event-chat meta to 99 years
	        set {_lp} to luckperms user from player
	        edit luckperms user {_lp}:
	            add {_prefix} to prefixes of user event-user
	        send "You now have admin!" to player
	""")
@Example("""
	command /mysuffix <string>:
	    permission: perk.suffix
	    cooldown: 5 seconds
	    trigger:
	        set {_suffix} to a luckperms suffix:
	            set value of event-chat meta to arg-1
	            set priority of event-chat meta to 25
	        set {_lp} to luckperms user from player
	        edit luckperms user {_lp}:
	            add {_suffix} to suffixes of user event-user
	        send formatted "Your suffix is now: %{_suffix}%<reset>!" to player
	""")
@Since({"1.0", "INSERT VERSION (no longer uses entries)"})
@SuppressWarnings("rawtypes")
public class ExprSecCreateChatMeta extends SectionExpression<ChatMetaNode> {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprSecCreateChatMeta.class, ChatMetaNode.class)
						.addPatterns("[a] [new] luckperm[s] (chat[ ]meta|prefix|suffix) [node] (with|from) value %string%")
						.supplier(ExprSecCreateChatMeta::new)
						.build()
		);

		eventValueRegistry.register(EventValue.builder(ChatMetaSectionEvent.class, ChatMetaNodeWrapper.class)
				.getter(ChatMetaSectionEvent::getNode)
				.patterns("(prefix|suffix|chat[ ]meta) [node]")
				.build());
	}

	private Expression<String> value;
	private ChatMetaType type;
	private Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItem) {
		type = parseResult.expr.contains("prefix") ? ChatMetaType.PREFIX : ChatMetaType.SUFFIX;

		value = (Expression<String>) expressions[0];

		if (sectionNode != null) {
			trigger = SectionUtils.loadLinkedCode("chat meta node section", (beforeLoading, afterLoading)
					-> loadCode(sectionNode, "chat meta node section", beforeLoading, afterLoading, ChatMetaSectionEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected ChatMetaNode @Nullable [] get(Event event) {
		ChatMetaNodeWrapper wrapper = new ChatMetaNodeWrapper(type);
		String value = this.value.getSingle(event);
		if (value != null)
			wrapper.setValue(value);


		ChatMetaSectionEvent sectionEvent = new ChatMetaSectionEvent(wrapper);
		if (trigger != null)
			Variables.withLocalVariables(event, sectionEvent, () -> TriggerItem.walk(trigger, sectionEvent));

		return new ChatMetaNode[] {sectionEvent.build()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<ChatMetaNode> getReturnType() {
		return ChatMetaNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("a new luckperms", type.toString())
				.appendIf(value != null, "from value", value)
				.toString();
	}

	public static class ChatMetaSectionEvent extends Event {

		public ChatMetaNodeWrapper node;

		public ChatMetaNodeWrapper getNode() {
			return this.node;
		}

		public ChatMetaNode build() {
			return node.build();
		}

		public ChatMetaSectionEvent(ChatMetaNodeWrapper node) {
			this.node = node;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}
