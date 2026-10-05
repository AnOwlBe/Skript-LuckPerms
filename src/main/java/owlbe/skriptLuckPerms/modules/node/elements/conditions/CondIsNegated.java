package owlbe.skriptLuckPerms.modules.node.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.luckperms.api.node.Node;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Is Negated")
@Description(""" 
		 Checks whether the given node(s) are negated or not.
		 """)
@Example("""
		if {_node} is negated:
		    broadcast "this node is negated!"
		else if {_node} isn't negated:
		    broadcast "this node is not negated!"
		""")
@Since("INSERT VERSION")
public class CondIsNegated extends Condition {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.CONDITION,
				PropertyCondition.infoBuilder(
								CondIsNegated.class, PropertyCondition.PropertyType.BE,
								"negated", "luckpermsnodes")
						.supplier(CondIsNegated::new)
						.build());
	}

	private Expression<Node> nodes;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		nodes = (Expression<Node>) expressions[0];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		return this.nodes.check(event, Node::isNegated, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return PropertyCondition.toString(this, PropertyCondition.PropertyType.BE, event, debug, nodes, "negated " + nodes.toString(event, debug));
	}

}

