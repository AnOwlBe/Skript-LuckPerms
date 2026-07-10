package owlbe.skriptLuckPerms.modules.meta.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.groups.elements.sections.SecEditGroup;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Name("Prefix")
@Description("""
		Returns the primary prefix of a user/group.
		If `prefixes` is used it will return a sorted list of all prefixes of the user/group.
		Use `priority of..` to get priority of a prefix.
		""")
@Example("""
		function get(p: offlineplayer):
			set {_lp} to luckperms user from {_p}
			set {_prefix} to formatted luckperms prefix of user {_lp}
			set {_prefixes::*} to luckperms prefixes of user {_lp}
			if {_p} is online:
				 send "Your prefix: %{_prefix}%" to {_p}
				 send "You have %size of {_prefixes::*}% prefixes!" to {_p}
				 loop {_prefixes::*}:
					  set {_prefix} to formatted loop-value
					  send "Priority: %priority of loop-value% Prefix: %{_prefix}%" to {_p}
		""")
@Example("""
		function get(group: string):
			set {_prefix} to formatted luckperms prefix of group {_group}
			set {_prefixes::*} to luckperms prefixes of group {_group}
			broadcast "%{_group}%'s primary prefix: %{_prefix}%"
			broadcast "Amount of all prefixes: %size of {_prefixes::*}%x"
			broadcast "All:"
				 loop {_prefixes::*}:
					  set {_prefix} to formatted loop-value
					  broadcast "Priority: %priority of loop-value% Prefix: %{_prefix}%"
		""")
@Since("1.0")
@SuppressWarnings("rawtypes")
public class ExprPrefix extends SimpleExpression<ChatMetaNode> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprPrefix.class, ChatMetaNode.class)
						.addPatterns(
								"[the] [luckperm[s]] prefix[:es] of group %luckpermsgroup%",
								"[the] [luckperm[s]] prefix[:es] of user %luckpermsuser%"
						)
						.build()
		);
	}

	private Expression<Group> groupExpr = null;
	private Expression<User> userExpr = null;
	private boolean isSingle;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		if (matchedPattern == 0) {
			groupExpr = (Expression<Group>) expressions[0];
		} else {
			userExpr = (Expression<User>) expressions[0];
		}
		isSingle = !parseResult.hasTag("es");
		return true;
	}

	@Override
	protected ChatMetaNode<?,?>[] get(Event event) {
		Collection<PrefixNode> nodes = null;
		if (groupExpr != null) {
			Group group = groupExpr.getSingle(event);
			if (group == null)
				return new ChatMetaNode[0];
			nodes = group.getNodes(NodeType.PREFIX);
		} else if (userExpr != null) {
			User user = userExpr.getSingle(event);
			if (user == null)
				return new ChatMetaNode[0];
			// sadly user.getNodes(NodeType.PREFIX) doesn't include inherited prefixes ;c
			nodes = user.resolveInheritedNodes(QueryOptions.nonContextual())
					.stream()
					.filter(NodeType.PREFIX::matches)
					.map(NodeType.PREFIX::cast)
					.collect(Collectors.toList());
		}

		if (nodes == null)
			return new ChatMetaNode[0];
		Stream<PrefixNode> stream = nodes.stream()
				.sorted(Comparator.comparingInt((PrefixNode node) -> node.getPriority()).reversed());
		if (isSingle) {
			return stream.findFirst().map(node -> new ChatMetaNode[]{node}).orElse(new ChatMetaNode[0]);
		} else {
			return stream.toArray(ChatMetaNode[]::new);
		}
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (groupExpr != null && !getParser().isCurrentEvent(SecEditGroup.GroupEvent.class)) {
			Skript.error("This can only be used inside an 'edit group' section");
			return null;
		}
		if (userExpr != null && !getParser().isCurrentEvent(SecEditUser.UserEvent.class)) {
			Skript.error("This can only be used inside an 'edit user' section");
			return null;
		}
		return switch (mode) {
			case SET, ADD, RESET, REMOVE ->
					CollectionUtils.array(ChatMetaNode[].class,ChatMetaNode.class); // ChatMetaNode.class is needed for SET (idk maybe better way)
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		Group group = groupExpr != null ? groupExpr.getSingle(event) : null;
		User user = userExpr != null ? userExpr.getSingle(event) : null;
		NodeMap data = group != null ? group.data() : user != null ? user.data() : null;
		if (data == null) return;
		switch (mode) {
			case ADD -> {
				if (isSingle) {
					Skript.warning("Cannot add to a primary prefix — use 'prefixes' instead.");
					return;
				}
				if (delta == null) return;
				ChatMetaNode node = (ChatMetaNode) delta[0];
				data.add(node);
			}
			case SET -> {
				if (delta == null) return;
				ChatMetaNode node = (ChatMetaNode) delta[0];
				if (!isSingle) {
					data.clear(NodeType.PREFIX::matches);
					data.add(node);
				} else {
					Collection<PrefixNode> nodes = group != null
							? group.getNodes(NodeType.PREFIX)
							: user != null ? user.getNodes(NodeType.PREFIX) : List.of();
					nodes.stream()
							.max(Comparator.comparingInt(ChatMetaNode::getPriority))
							.ifPresent(data::remove);
					data.add(node);
				}
			}
			case REMOVE -> {
				if (isSingle) {
					Skript.warning("Cannot remove a single prefix — use reset to clear or 'prefixes' to remove specific ones.");
					return;
				}
				if (delta == null)
					return;
				ChatMetaNode node = (ChatMetaNode) delta[0];
				data.remove(node);
			}
			case RESET -> data.clear(NodeType.PREFIX::matches);
		}
	}

	@Override
	public boolean isSingle() {
		return isSingle;
	}

	@Override
	public Class<? extends ChatMetaNode> getReturnType() {
		return ChatMetaNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append(isSingle ? "prefix" : "prefixes", "of")
				.append(groupExpr != null ? groupExpr : userExpr)
				.toString();
	}

}
