package owlbe.skriptLuckPerms.modules.users.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;
import java.util.List;

@Name("Groups Of User")
@Description("""
		Represents the groups of a user.
		See <a href='#EffGrantGroup'>grant permission</a> for how to add groups to a user.
		""")
@Example("""
		function example(p: offlineplayer):
			set {_lp} to luckperms user from {_p}
			broadcast "%{_p}% has %size of luckperms groups of {_lp}% groups!"
			broadcast "their groups: %luckperms groups of {_lp}%"
		""")
@Since("1.0")
public class ExprGroupsOfUser extends PropertyExpression<User, Group> {

	public static void register(SyntaxRegistry registry) {
		registry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprGroupsOfUser.class,
						Group.class,
						"luckperm[s] groups",
						"luckpermsuser",
						false
				)
						.supplier(ExprGroupsOfUser::new)
						.build()
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<User>) expressions[0]);
		return true;
	}

	@Override
	protected Group[] get(Event event, User[] users) {
		return Arrays.stream(users)
				.flatMap(user -> user.getInheritedGroups(QueryOptions.nonContextual()).stream())
				.toArray(Group[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Group> getReturnType() {
		return Group.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("luckperms groups of")
				.toString();
	}

}
