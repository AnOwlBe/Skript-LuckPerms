package owlbe.skriptLuckPerms.modules.permholder.group.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.ArrayList;
import java.util.List;

@Name("Groups")
@Description("""
		Represents the groups of a LuckPerms user, group or track.
		
		For a user, these are the groups the user has.
		For a group, these are the parent groups of the group.
		For a track, these are the groups in the track.
		
		See <a href='#EffGrantGroup'>grant group</a> for how to add groups to a user.
		""")
@Example("""
		function example(p: offlineplayer):
			set {_lp} to luckperms user from {_p}
			broadcast "%{_p}% has %size of luckperms groups of {_lp}% groups!"
			broadcast "their groups: %luckperms groups of {_lp}%"
		""")
@Since("1.0, INSERT VERSION (supports groups and tracks)")
public class ExprGroups extends PropertyExpression<Object, Group> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				infoBuilder(
						ExprGroups.class,
						Group.class,
						"luckperm[s] groups",
						"luckpermsuser/luckpermsgroup/luckpermstrack",
						false
				)
						.supplier(ExprGroups::new)
						.build()
		);
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr(expressions[0]);
		return true;
	}

	@Override
	protected Group[] get(Event event, Object[] targets) {
		List<Group> groups = new ArrayList<>();

		for (Object object : targets) {
			switch (object) {
				case User user -> groups.addAll(user.getInheritedGroups(QueryOptions.nonContextual()));
				case Group group -> groups.addAll(group.getInheritedGroups(QueryOptions.nonContextual()));
				case Track track -> track.getGroups().forEach(name -> {
					Group group = LuckPermsProvider.get().getGroupManager().getGroup(name);
					if (group != null)
						groups.add(group);
				});
				default -> {}
			}
		}

		return groups.toArray(new Group[0]);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
		return switch (mode) {
			case SET, ADD, RESET, REMOVE -> CollectionUtils.array(PermissionNode.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
		Object target = getExpr().getSingle(event);
		if (target == null)
			return;

		if (!(target instanceof Track track)) {
			error("You can only change the groups of a track. Please see the inheritances expression for changing the groups of a user/group.");
			return;
		}

		if (mode == Changer.ChangeMode.RESET) {
			track.clearGroups();
			return;
		}

		Group group = delta != null ? (Group) delta[0] : null;
		if (group == null)
			return;

		switch (mode) {
			case SET -> {
				track.clearGroups();
				track.appendGroup(group);
			}
			case ADD -> track.appendGroup(group);
			case REMOVE -> track.removeGroup(group);
		}
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
		return "luckperms groups of " + getExpr().toString(event, debug);
	}

}
