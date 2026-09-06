package owlbe.skriptLuckPerms.modules.permholder.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.permholder.PermHolderUtils;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;

import java.util.Arrays;
import java.util.Comparator;

@Name("Prefix")
@Description("""
		The prefix(es) of a permission holder (a user or group).
		
		`prefix` refers to the holder's primary prefix while `prefixes` refers to all of their prefixes.
		
		Getting the prefix(es) resolves inherited nodes (e.g. from parent groups) and also the holder's own prefix(es).
		However, setting, adding, or removing prefixes only affects nodes set directly on the holder - inherited prefixes \
		from parent groups cannot be changed this way, instead, consider setting the prefix(es) of the group.
		""")
@Example("""
        # Chat formatting!
        on chat:
            set {_message} to unformatted message
            set {_user} to quick luckperms user from player
            set {_prefix} to the luckperms prefix of {_user} # their primary prefix
            set the chat format to formatted "%{_prefix}% %name of player% <reset><dark_gray>: <reset>%{_message}%"
        """)
@Example("""
		# A perk to allow players to change their prefix
		command /myprefix <string>:
		    permission: perk.prefix
		    trigger:
		        set {_user} to luckperms user from player
		        if value of (luckperms prefix of {_user}) is arg-1:
		            send "Your prefix is already this!" to player
		            stop
		        set {_prefix} to a luckperms prefix from value arg-1
		        edit luckperms user {_user}:
		            set luckperms prefix of event-user to {_prefix}
		        send "Successfully set your primary prefix to '%arg-1%'" to player
		""")
@Since({"1.0", "INSERT VERSION ('luckperms prefixes' changed pattern)"})
@SuppressWarnings("rawtypes")
public class ExprPrefix extends PropertyExpression<PermissionHolder, ChatMetaNode> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprPrefix.class,
						ChatMetaNode.class,
						"luckperm[s] prefix[plural:es]",
						"luckpermspermissionholder",
						false
				)
						.supplier(ExprPrefix::new)
						.build()
		);
	}

	private boolean plural;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<PermissionHolder>) expressions[0]);
		plural = parseResult.hasTag("plural");
		return true;
	}

	@Override
	protected ChatMetaNode<?, ?>[] get(Event event, PermissionHolder[] holders) {
		if (plural) {
			return Arrays.stream(holders)
					.flatMap(holder -> Arrays.stream(PermHolderUtils.getPrefixes(holder)))
					.toArray(ChatMetaNode<?, ?>[]::new);

		}

		return Arrays.stream(holders)
				.map(PermHolderUtils::getPrimaryPrefix)
				.toArray(ChatMetaNode<?, ?>[]::new);
	}


	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (!getParser().isCurrentEvent(HolderSectionEvent.class)) {
			Skript.error("You can only change the prefix(es) of a holder inside a 'edit permission holder' section");
			return null;
		}

		return switch (mode) {
			case SET, ADD, RESET, REMOVE -> CollectionUtils.array(ChatMetaNode[].class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		PermissionHolder holder = getExpr().getSingle(event);
		if (holder == null)
			return;

		NodeMap data = holder.data();

		ChatMetaNode<?, ?> node = delta != null ? (ChatMetaNode<?, ?>) delta[0] : null;

		switch (mode) {
			case SET -> {
				if (node == null)
					return;

				if (plural) {
					data.clear(NodeType.PREFIX::matches);

					data.add(node);
				} else {
					int priority = holder.getNodes(NodeType.PREFIX).stream()
							.mapToInt(PrefixNode::getPriority)
							.max()
							.orElse(0);

					holder.getNodes(NodeType.PREFIX).stream()
							.max(Comparator.comparingInt(PrefixNode::getPriority))
							.ifPresent(data::remove);

					PrefixNode newNode = PrefixNode.builder(node.getMetaValue(), priority)
							.context(node.getContexts())
							.expiry(node.getExpiry())
							.build();

					data.add(newNode);
				}
			}
			case ADD -> {
				if (!plural) {
					Skript.warning("Cannot add to a holder's primary prefix - use 'prefixes' instead.");
					return;
				}

				if (node == null)
					return;

				data.add(node);
			}
			case RESET -> {
				if (!plural) {
					holder.getNodes(NodeType.PREFIX).stream()
							.max(Comparator.comparingInt(PrefixNode::getPriority))
							.ifPresent(data::remove);
					return;
				}

				data.clear(NodeType.PREFIX::matches);
			}
			case REMOVE -> {
				if (!plural) {
					Skript.warning("Cannot remove a single prefix - use reset to clear or 'prefixes' to remove specific ones.");
					return;
				}
				if (node == null)
					return;

				data.remove(node);
			}
		}
	}

	@Override
	public boolean isSingle() {
		return !plural;
	}

	@Override
	public Class<? extends ChatMetaNode> getReturnType() {
		return ChatMetaNode.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("luckperms")
				.append(plural ? "prefixes" : "prefix", "of", getExpr())
				.toString();
	}

}
