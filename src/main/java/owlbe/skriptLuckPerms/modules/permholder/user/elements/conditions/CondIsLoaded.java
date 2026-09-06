package owlbe.skriptLuckPerms.modules.permholder.user.elements.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.UUID;

@Name("Is Loaded")
@Description(""" 
		 Checks whether a user from the given UUID is loaded or not.
		 """)
@Example("""
		on death:
		    set the death message to ""
		    if luckperms user from id (uuid of victim) is not loaded:
		        set {_user} to luckperms user from victim
		    else:
		        set {_user} to quick luckperms user from victim
		    broadcast "%luckperms prefix of {_user}%%player% <reset>just died!"
		""")
@Since("INSERT VERSION")
public class CondIsLoaded extends Condition {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.CONDITION,
				SyntaxInfo.builder(CondIsLoaded.class)
						.addPatterns(
								"luckperm[s] user[s] (with|from) [uu]id[s] %uuids% (is|are) loaded",
								"luckperm[s] user[s] (with|from) [uu]id[s] %uuids% (is not|isn't|are not|aren't) loaded"
						)
						.supplier(CondIsLoaded::new)
						.build()
		);
	}

	private Expression<UUID> uuids;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		uuids = (Expression<UUID>) expressions[0];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		UserManager manager = LuckPermsProvider.get().getUserManager();
		return this.uuids.check(event, manager::isLoaded, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (isNegated())
			return "luckperms users from uuid " + uuids.toString(event, debug) + " aren't loaded";
		return "luckperms users from uuid " + uuids.toString(event, debug) + " are loaded";
	}

}
