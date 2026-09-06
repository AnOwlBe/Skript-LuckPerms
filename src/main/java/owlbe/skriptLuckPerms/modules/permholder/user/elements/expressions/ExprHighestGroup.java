package owlbe.skriptLuckPerms.modules.permholder.user.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;

@Name("Highest Group")
@Description("""
		Represents the highest group of a LuckPerms user.
		""")
@Example("""
		command /myhighestgroup:
		    trigger:
		        send "Your highest group is.."
		        wait 2 seconds
		        set {_lp} to luckperms user from player
		        send "%highest luckperms group of {_lp}%" to player
		""")
@Since("1.0")
public class ExprHighestGroup extends PropertyExpression<User, Group> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprHighestGroup.class,
						Group.class,
						"highest luckperm[s] group",
						"luckpermsuser",
						false
				)
						.supplier(ExprHighestGroup::new)
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
				.map(user -> LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup()))
				.toArray(Group[]::new);

	}

	@Override
	public boolean isSingle() {
		return getExpr().isSingle();
	}

	@Override
	public Class<? extends Group> getReturnType() {
		return Group.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "highest luckperms group of " + getExpr().toString(event, debug);
	}

}
