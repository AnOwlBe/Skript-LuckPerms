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
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.permholder.PermHolderUtils;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;

import java.util.Arrays;
import java.util.Comparator;

@Name("Suffix")
@Description("""
		The suffix(es) of a permission holder (a user or group).
		
		`suffix` refers to the holder's primary suffix while `suffix` refers to all of their suffixes.
		
		Getting the suffix(es) resolves inherited nodes (e.g. from parent groups) and also the holder's own suffix(es).
		However, setting, adding, or removing suffixes only affects nodes set directly on the holder - inherited suffixes \
		from parent groups cannot be changed this way, instead, consider setting the suffix(es) of the group.
		""")
@Example("""
        
        """)
@Example("""
		
		""")
@Since({"1.0", "INSERT VERSION ('luckperms suffixes' changed pattern)"})
@SuppressWarnings("rawtypes")
public class ExprSuffix extends PropertyExpression<PermissionHolder, ChatMetaNode> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprSuffix.class,
						ChatMetaNode.class,
						"luckperm[s] suffix[plural:es]",
						"luckpermspermissionholder",
						false
				)
						.supplier(ExprSuffix::new)
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
					.flatMap(holder -> Arrays.stream(PermHolderUtils.getSuffixes(holder)))
					.toArray(ChatMetaNode<?, ?>[]::new);

		}

		return Arrays.stream(holders)
				.map(PermHolderUtils::getPrimarySuffix)
				.toArray(ChatMetaNode<?, ?>[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (!getParser().isCurrentEvent(HolderSectionEvent.class)) {
			Skript.error("You can only change the suffix(es) of a holder inside a 'edit permission holder' section");
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
					data.clear(NodeType.SUFFIX::matches);

					data.add(node);
				} else {
					int priority = holder.getNodes(NodeType.SUFFIX).stream()
							.mapToInt(SuffixNode::getPriority)
							.max()
							.orElse(0);

					holder.getNodes(NodeType.SUFFIX).stream()
							.max(Comparator.comparingInt(SuffixNode::getPriority))
							.ifPresent(data::remove);

					SuffixNode newNode = SuffixNode.builder(node.getMetaValue(), priority)
							.context(node.getContexts())
							.expiry(node.getExpiry())
							.build();

					data.add(newNode);
				}
			}
			case ADD -> {
				if (!plural) {
					Skript.warning("Cannot add to a holder's primary suffix - use 'suffixes' instead.");
					return;
				}

				if (node == null)
					return;

				data.add(node);
			}
			case RESET -> {
				if (!plural) {
					holder.getNodes(NodeType.SUFFIX).stream()
							.max(Comparator.comparingInt(SuffixNode::getPriority))
							.ifPresent(data::remove);
					return;
				}

				data.clear(NodeType.SUFFIX::matches);
			}
			case REMOVE -> {
				if (!plural) {
					Skript.warning("Cannot remove a single suffix - use reset to clear or 'suffixes' to remove specific ones.");
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
				.append(plural ? "suffixes" : "suffix", "of", getExpr())
				.toString();
	}

}
