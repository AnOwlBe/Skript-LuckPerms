package owlbe.skriptLuckPerms.modules.users.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangerUtils;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.matcher.NodeMatcher;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static org.skriptlang.skript.lang.script.ScriptWarning.printDeprecationWarning;

@Name("Group Members")
@Description(""" 
		 Returns a list of UUIDS of users who have the specified group.
		 Should be relatively fast depending on how many users have a group.
		 """)
@Example("""
		function search():
			get the members of group "example" and store it in {_lp::*}
			send "%size of {_lp::*}% have 'example' group!" to all ops
		""")
@Since("1.0")
public class EffGroupMembers extends AsyncEffect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffGroupMembers.class)
						.addPatterns(
								"set %-~objects% to [the] [luckperm[s]] (members|players) (of|in) group %luckpermsgroup%",
								"(fetch|get) [the] [luckperm[s]] (members|players) (of|in) group %luckpermsgroup% and store (it|the result) in %-~objects%")
						.build()
		);
	}

	private Expression<Group> group;
	private Expression<?> variable;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);
		if (matchedPattern == 0) {
			group = (Expression<Group>) expressions[1];
			variable = expressions[0];
		} else {
			group = (Expression<Group>) expressions[0];
			variable = expressions[1];
			printDeprecationWarning("This pattern is deprecated and scheduled for removal. Please consider using the other pattern.");
		}
		if (!ChangerUtils.acceptsChange(variable, ChangeMode.SET, UUID.class)) {
			Skript.error(variable.toString(null, Skript.debug()) + " cannot be set to multiple UUIDS.");
			return false;
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		Group group = this.group.getSingle(event);
		if (group == null)
			return;

		LuckPermsProvider.get().getUserManager()
				.searchAll(NodeMatcher.key((Node) InheritanceNode.builder(group).build()))
				.thenAccept(results -> variable.change(event, results.keySet().toArray(), ChangeMode.SET))
				.join();
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("set", variable, "to luckperms members in group", group)
				.toString();
	}

}
