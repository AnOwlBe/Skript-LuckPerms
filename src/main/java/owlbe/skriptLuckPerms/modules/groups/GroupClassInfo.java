package owlbe.skriptLuckPerms.modules.groups;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.WeightNode;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import owlbe.skriptLuckPerms.modules.groups.elements.sections.SecEditGroup;

import javax.annotation.Nullable;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.WEIGHT;
import static owlbe.skriptLuckPerms.skript.properties.Properties.getProperty;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class GroupClassInfo extends ClassInfo<Group> {

	public GroupClassInfo() {
		super(Group.class, "luckpermsgroup");
		this.user("luckperms ?groups?")
				.name("Group")
				.description("Represents a LuckPerms group.")
				.since("1.0")
				.parser(new GroupParser())
				.defaultExpression(new EventValueExpression<>(Group.class))
				.property(getProperty(WEIGHT),
						"The weight of a group.",
						addon,
						new GroupWeightHandler());
	}

	private static class GroupParser extends Parser<Group> {
		//<editor-fold desc="group parser" defaultstate="collapsed">
		@Override
		@Nullable
		public Group parse(String s, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(Group group, int flags) {
			return group.getName();
		}

		@Override
		public String toVariableNameString(Group group) {
			return group.getName();
		}

		//</editor-fold>
	}

	private static class GroupWeightHandler implements ExpressionPropertyHandler<Group, Integer> {
		//<editor-fold desc="group weight handler" defaultstate="collapsed">

		@Override
		public @Nullable Integer convert(Group group) {
			return group.getWeight().isPresent() ? group.getWeight().getAsInt() : 0;
		}


		@Override
		public Class<?>[] acceptChange(ChangeMode mode) {
			if (!(ParserInstance.get().isCurrentEvent(SecEditGroup.GroupEvent.class))) {
				Skript.error("You can only change weight of a group in an edit group section.");
				return null;
			}
			return switch (mode) {
				case SET, ADD, REMOVE, RESET -> CollectionUtils.array(Integer.class);
				default -> null;
			};
		}

		@Override
		public void change(Group group, Object[] delta, ChangeMode mode) {
			int amount = delta != null ? (Integer) delta[0] : 0;
			int groupWeight = group.getWeight().isPresent() ? group.getWeight().getAsInt() : 0;
			switch (mode) {
				case SET -> {
					group.data().clear(NodeType.WEIGHT::matches);
					group.data().add(WeightNode.builder(Math.max(0, amount)).build());
				}
				case ADD -> {
					group.data().clear(NodeType.WEIGHT::matches);
					group.data().add(WeightNode.builder(Math.max(0, groupWeight + amount)).build());
				}
				case REMOVE -> {
					group.data().clear(NodeType.WEIGHT::matches);
					group.data().add(WeightNode.builder(Math.max(0, groupWeight - amount)).build());
				}
				case RESET -> group.data().clear(NodeType.WEIGHT::matches);
			}
		}

		@Override
		public @NotNull Class<Integer> returnType() {
			return Integer.class;
		}
		//</editor-fold>
	}

}
