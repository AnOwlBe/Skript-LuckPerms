package owlbe.skriptLuckPerms.modules.permholder.group;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.WeightNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.log.runtime.RuntimeErrorProducer;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;

import static org.skriptlang.skript.lang.properties.Property.DISPLAY_NAME;
import static org.skriptlang.skript.lang.properties.Property.NAME;
import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.WEIGHT;
import static owlbe.skriptLuckPerms.skript.properties.Properties.getProperty;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class GroupClassInfo extends ClassInfo<Group> {

	public GroupClassInfo() {
		super(Group.class, "luckpermsgroup");
		this.user("luckperms ?groups?")
				.name("LuckPerms Group")
				.description("Represents a LuckPerms group.")
				.since("1.0")
				.parser(new GroupParser())
				.defaultExpression(new EventValueExpression<>(Group.class))
				.property(getProperty(WEIGHT),
						"The weight of this group. Can be changed.",
						addon,
						new GroupWeightHandler())
				.property(NAME,
						"The name of this group.",
						addon,
						new GroupNameHandler())
				.property(DISPLAY_NAME,
						"The display name of this group, if it has one that differs from it's actual name. Can be set or reset.",
						addon,
						new GroupDisplayNameHandler());

	}

	private static class GroupParser extends Parser<Group> {
		//<editor-fold desc="group parser" defaultstate="collapsed">
		@Override
		@Nullable
		public Group parse(String string, ParseContext context) {
			if (context == ParseContext.COMMAND || context == ParseContext.PARSE) {
				if (string.isEmpty())
					return null;
			}
			return LuckPermsProvider.get().getGroupManager().getGroup(string);
		}

		@Override
		public String toString(Group group, int flags) {
			return "group '" + group.getName() + "'";
		}

		@Override
		public String toVariableNameString(Group group) {
			return group.getName();
		}

		//</editor-fold>
	}

	private static class GroupWeightHandler implements ExpressionPropertyHandler<Group, Integer> {
		//<editor-fold desc="group weight handler" defaultstate="collapsed">

		private boolean shouldError = false;
		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable Integer convert(Group group) {
			return group.getWeight().orElse(0);
		}


		@Override
		public Class<?>[] acceptChange(ChangeMode mode) {
			if (!(ParserInstance.get().isCurrentEvent(HolderSectionEvent.class)))
				shouldError = true;

			return switch (mode) {
				case SET, ADD, REMOVE, RESET -> CollectionUtils.array(Integer.class);
				default -> null;
			};
		}

		@Override
		public void change(Group group, Object[] delta, ChangeMode mode) {
			if (shouldError && producer != null) {
				producer.error("You can only change the weight of a group inside a 'edit permission holder' section");
				return;
			}

			int amount = delta != null ? (Integer) delta[0] : 0;
			int weight = group.getWeight().orElse(0);

			group.data().clear(NodeType.WEIGHT::matches);

			int newWeight = switch (mode) {
				case SET -> amount;
				case ADD -> weight + amount;
				case REMOVE -> weight - amount;
				default -> weight;
			};
			group.data().add(WeightNode.builder(Math.max(0, newWeight)).build());
		}

		@Override
		public @NotNull Class<Integer> returnType() {
			return Integer.class;
		}
		//</editor-fold>
	}

	private static class GroupNameHandler implements ExpressionPropertyHandler<Group, String> {
		//<editor-fold desc="group name handler" defaultstate="collapsed">

		@Override
		public @Nullable String convert(Group group) {
			return group.getName();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

	private static class GroupDisplayNameHandler implements ExpressionPropertyHandler<Group, String> {
		//<editor-fold desc="group display name handler" defaultstate="collapsed">

		private boolean shouldError = false;
		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable String convert(Group group) {
			return group.getDisplayName();
		}

		@Override
		public Class<?>[] acceptChange(ChangeMode mode) {
			// TODO: Use parse time erroring not run time errors once this once skript fixes the type property issue
			if (!(ParserInstance.get().isCurrentEvent(HolderSectionEvent.class)))
				shouldError = true;

			return switch (mode) {
				case SET, RESET -> CollectionUtils.array(String.class);
				default -> null;
			};
		}

		@Override
		public void change(Group group, Object[] delta, ChangeMode mode) {
			if (shouldError && producer != null) {
				producer.error("You can only change the display name of a group inside a 'edit permission holder' section");
				return;
			}

			String newDisplayName = delta != null ? (String) delta[0] : "";

			group.data().clear(NodeType.META.predicate(node -> node.getMetaKey().equals("displayname")));

			if (mode == ChangeMode.SET) {
				group.data().add(MetaNode.builder("displayname", newDisplayName)
						.build());
			}
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

}
