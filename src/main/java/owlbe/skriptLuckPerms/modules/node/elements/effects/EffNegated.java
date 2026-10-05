package owlbe.skriptLuckPerms.modules.node.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.luckperms.wrapper.NodeWrapper;
import owlbe.skriptLuckPerms.utils.events.SectionEvent;

@Name("Negated")
@Description("""
	    Makes the given LuckPerms node negated or not.
	    """)
@Example("""
		set {_perm} to a new luckperms perm from key "perk.fly":
		    make event-permission negated
		""")
@Since("INSERT VERSION")
public class EffNegated extends Effect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(SyntaxRegistry.EFFECT, SyntaxInfo.builder(EffNegated.class)
				.addPatterns(
						"make %luckpermsnodewrappers% negated",
						"negate %luckpermsnodewrappers%",
						"make %luckpermsnodewrappers% (not|no longer) negated"
				)
				.supplier(EffNegated::new)
				.build());
	}

	private Expression<NodeWrapper> nodes;
	private int matchedPattern;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		nodes = (Expression<NodeWrapper>) expressions[0];
		this.matchedPattern = matchedPattern;

		if (!(ParserInstance.get().isCurrentEvent(SectionEvent.class))) {
			Skript.error("You can only negate a node inside a 'node' section");
			return false;
		}

		return true;
	}

	@Override
	protected void execute(Event event) {
		boolean value = matchedPattern == 2;

		for (NodeWrapper node : nodes.getArray(event))
			node.value(value);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("make", nodes)
				.append(matchedPattern == 2 ? "no longer negated" : "negated")
				.toString();
	}

}
