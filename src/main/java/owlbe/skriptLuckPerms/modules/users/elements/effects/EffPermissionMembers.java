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
import net.luckperms.api.node.Node;
import net.luckperms.api.node.matcher.NodeMatcher;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.UUID;

@Name("Permission Members")
@Description(""" 
		 Returns a list of UUIDS of users who have the specified permission.
		 Should be relatively fast depending on how many users have said permission.
		 """)
@Example("""
		function search():
			get the users with perm "example" and store it in {_lp::*}
			set {_lp::*} to all luckperms users with permission "example"
			send "%size of {_lp::*}% have 'example' permission!" to all ops
		""")
@Since("1.0")
public class EffPermissionMembers extends AsyncEffect {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EFFECT,
				SyntaxInfo.builder(EffPermissionMembers.class)
						.addPattern("set %-~objects% to (all|all of the) [luckperm[s]] (users|players) with perm[ission] %luckpermspermission%")
						.build()
		);
	}

	private Expression<PermissionNode> permission;
	private Expression<?> variable;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		getParser().setHasDelayBefore(Kleenean.TRUE);
		permission = (Expression<PermissionNode>) expressions[1];
		variable = expressions[0];
		if (!ChangerUtils.acceptsChange(variable, ChangeMode.SET, UUID.class)) {
			Skript.error(variable.toString(null, Skript.debug()) + " cannot be set to UUIDS.");
			return false;
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		PermissionNode permission = this.permission.getSingle(event);
		if (permission == null)
			return;
		var results = LuckPermsProvider.get().getUserManager()
				.searchAll(NodeMatcher.key(Node.builder(permission.getKey()).build()))
				.join();
		variable.change(event, results.keySet().toArray(), ChangeMode.SET);

	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("set", variable)
				.append("to all players with permission", permission)
				.toString();
	}

}
