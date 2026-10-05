package owlbe.skriptLuckPerms.modules.test.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.NoDoc;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Description("""
		Checks whether a timespan is within 3 seconds of another timespan.
		""")
@NoDoc
@Since("INSERT VERSION")
// Utility for checking `expiry of` since it can be a milli off and make tests fail
public class CondAroundTimespan extends Condition {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.CONDITION,
				PropertyCondition.infoBuilder(
								CondAroundTimespan.class, PropertyCondition.PropertyType.BE,
								"around [the] time[span] %timespan%", "timespans")
						.supplier(CondAroundTimespan::new)
						.build());
	}

	private Expression<Timespan> first;
	private Expression<Timespan> second;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		first = (Expression<Timespan>) expressions[0];
		second = (Expression<Timespan>) expressions[1];
		setNegated(matchedPattern == 1);

		return true;
	}

	@Override
	public boolean check(Event event) {
		return this.first.check(event, timespan -> {
			Timespan expected = this.second.getSingle(event);
			if (expected == null)
				return false;

			long differenceMillis = timespan.getDuration().toMillis() - expected.getDuration().toMillis();
			return differenceMillis >= -3000 && differenceMillis <= 3000;
		}, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return PropertyCondition.toString(this, PropertyCondition.PropertyType.BE, event, debug, this.first, "around timespan " + this.second.toString(event, debug));
	}

}
