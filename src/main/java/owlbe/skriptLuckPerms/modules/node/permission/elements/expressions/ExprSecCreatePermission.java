package owlbe.skriptLuckPerms.modules.node.permission.elements.expressions;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.utils.wrapper.PermissionNodeWrapper;

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
public class ExprSecCreatePermission extends SectionExpression<PermissionNode> {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprSecCreatePermission.class, PermissionNode.class)
						.addPatterns("[a] [new] luckperm[s] perm[ission] [node] (with|from) (key|id|value) %string%")
						.supplier(ExprSecCreatePermission::new)
						.build()
		);

		eventValueRegistry.register(EventValue.builder(PermissionSectionEvent.class, PermissionNodeWrapper.class)
				.getter(event -> event.node)
				.patterns("perm[ission] [node]")
				.build());
	}

	private @Nullable Expression<String> key;
	private Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItem) {
		if (expressions[0] != null)
			key = (Expression<String>) expressions[0];

		if (sectionNode != null) {
			trigger = SectionUtils.loadLinkedCode("permission node section", (beforeLoading, afterLoading)
					-> loadCode(sectionNode, "permission node section", beforeLoading, afterLoading, owlbe.skriptLuckPerms.modules.node.chatmeta.elements.expressions.ExprSecCreateChatMeta.ChatMetaSectionEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected PermissionNode @Nullable [] get(Event event) {
		String key = null;
		if (this.key != null)
			key = this.key.getSingle(event);

		PermissionNodeWrapper wrapper = new PermissionNodeWrapper(key);

		PermissionSectionEvent sectionEvent = new PermissionSectionEvent(wrapper);
		if (trigger != null)
			Variables.withLocalVariables(event, sectionEvent, () -> TriggerItem.walk(trigger, sectionEvent));

		return new PermissionNode[] {sectionEvent.build()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<PermissionNode> getReturnType() {
		return PermissionNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (key == null)
			return "a new luckperms permission";

		return "a new luckperms permission from key" + key.toString(event, debug);
	}

	public static class PermissionSectionEvent extends Event {

		public PermissionNodeWrapper node;

		public PermissionNode build() {
			return node.build();
		}

		public PermissionSectionEvent(PermissionNodeWrapper node) {
			this.node = node;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}
