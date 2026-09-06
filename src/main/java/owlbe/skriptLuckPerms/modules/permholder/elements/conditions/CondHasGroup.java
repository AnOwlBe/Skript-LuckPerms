package owlbe.skriptLuckPerms.modules.permholder.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Set;
import java.util.stream.Collectors;

@Name("Has LuckPerms Group")
@Description(""" 
		 Checks whether a permission holder (a user or group) has the given LuckPerms group.
		 """)
@Example("""
		function hasGroup(p: player,group: string) :: boolean:
			set {_lp} to luckperms user from {_p}
			if {_lp} has luckperms group {_group}:
				return true
			else:
				return false
		""")
@Since("1.0")
public class CondHasGroup extends Condition {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.CONDITION,
				PropertyCondition.infoBuilder(
								CondHasGroup.class, PropertyCondition.PropertyType.HAVE,
								"luckperm[s] group[s] %luckpermsgroups%", "luckpermspermissionholders")
						.supplier(CondHasGroup::new)
						.build());
	}

	private Expression<PermissionHolder> holders;
	private Expression<Group> groups;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		holders = (Expression<PermissionHolder>) expressions[0];
		groups = (Expression<Group>) expressions[1];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		return this.holders.check(event, holder -> {
			Set<String> groupNames = holder.getNodes(NodeType.INHERITANCE).stream()
					.map(InheritanceNode::getGroupName)
					.collect(Collectors.toSet());
			if (groupNames.isEmpty())
				return isNegated();

			return SimpleExpression.check(groups.getArray(event), group -> groupNames.contains(group.getName()), false, groups.getAnd());
		}, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return PropertyCondition.toString(this, PropertyCondition.PropertyType.HAVE, event, debug, holders, "luckperms group " + groups.toString(event, debug));
	}

}
