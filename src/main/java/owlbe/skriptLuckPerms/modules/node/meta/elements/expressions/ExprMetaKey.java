package owlbe.skriptLuckPerms.modules.node.meta.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;
import owlbe.skriptLuckPerms.utils.wrapper.MetaNodeWrapper;

import java.util.Arrays;

@Name("Meta Key")
@Description("""
		The key of a meta node.
		""")
@Example("""
		set {_key} to luckperms meta key of {_meta}
		""")
@Since("INSERT VERSION")
public class ExprMetaKey extends PropertyExpression<Object, String> {

	// TODO: Change this to be a type property (key of) when Skript changes theirs
	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprMetaKey.class,
						String.class,
						"[luckperm[s]] meta key",
						"luckpermsmeta/luckpermsmetawrapper",
						false
				)
						.supplier(ExprMetaKey::new)
						.build()
		);
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr(expressions[0]);
		return true;
	}

	@Override
	protected String[] get(Event event, Object[] nodes) {
		return Arrays.stream(nodes)
				.map(node -> switch (node) {
					case MetaNode metaNode -> metaNode.getMetaKey();
					case MetaNodeWrapper metaNodeWrapper -> metaNodeWrapper.getKey();
					default -> throw new IllegalArgumentException("Unexpected node type: " + node.getClass());
				})
				.toArray(String[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (!getParser().isCurrentEvent(HolderSectionEvent.class)) {
			Skript.error("You can only change the permissions of a holder inside an 'edit permission holder' section");
			return null;
		}

		return switch (mode) {
			case SET, RESET -> CollectionUtils.array(String.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		Object node = getExpr().getSingle(event);
		if (!(node instanceof MetaNodeWrapper metaNode)) // can only change the key of the wrapper version
			return;

		if (delta != null)
			metaNode.setKey((String) delta[0]);

	}

	@Override
	public boolean isSingle() {
		return getExpr().isSingle();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "meta key of " + getExpr().toString(event, debug);
	}

}


