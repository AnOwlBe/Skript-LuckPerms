package owlbe.skriptLuckPerms.modules.node.chatmeta;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.coll.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.log.runtime.RuntimeErrorProducer;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.luckperms.wrapper.ChatMetaNodeWrapper;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;
import static owlbe.skriptLuckPerms.skript.properties.Properties.PRIORITY;
import static owlbe.skriptLuckPerms.utils.PropertyUtils.getProperty;

@SuppressWarnings({"UnstableApiUsage"})
public class ChatMetaWrapperClassInfo extends ClassInfo<ChatMetaNodeWrapper> {

	public ChatMetaWrapperClassInfo() {
		super(ChatMetaNodeWrapper.class, "luckpermschatmetawrapper");
		this.user("luckperms chatmeta ?wrappers?")
				.name(NO_DOC)
				.description(NO_DOC)
				.since("INSERT VERSION")
				.defaultExpression(new EventValueExpression<>(ChatMetaNodeWrapper.class))
				.property(getProperty(PRIORITY, ExpressionPropertyHandler.class),
						"The priority of this chat meta. Can be changed.",
						SkriptLuckPerms.getAddonInstance(),
						new ChatMetaPriorityHandler())
				.property(TYPED_VALUE,
						"The value of this chat meta. Can be changed.",
						SkriptLuckPerms.getAddonInstance(),
						new ChatMetaValueHandler());
	}

	private static class ChatMetaValueHandler implements TypedValueHandler<ChatMetaNodeWrapper, String> {
		//<editor-fold desc="chat meta node value handler" defaultstate="collapsed">

		@Override
		public @Nullable String convert(ChatMetaNodeWrapper node) {
			return node.metaValue();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

	private static class ChatMetaPriorityHandler implements ExpressionPropertyHandler<ChatMetaNodeWrapper, Integer>{
		//<editor-fold desc="chat meta priority handler" defaultstate="collapsed">

		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable Integer convert(ChatMetaNodeWrapper node) {
			return node.priority();
		}

		@Override
		public Class<?>[] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, ADD, REMOVE, RESET -> CollectionUtils.array(Integer.class);
				default -> null;
			};
		}

		@Override
		public void change(ChatMetaNodeWrapper node, Object[] delta, ChangeMode mode) {
			if (node.built() && producer != null) {
				producer.error("You can only change the priority of a chat meta node inside a 'chat meta' section.");
				return;
			}

			int amount = delta != null ? (Integer) delta[0] : 0;
			int priority = node.priority();

			int newPriority = switch (mode) {
				case SET -> amount;
				case ADD -> priority + amount;
				case REMOVE -> priority - amount;
				default -> priority;
			};

			node.priority(newPriority);

		}

		@Override
		public @NotNull Class<Integer> returnType() {
			return Integer.class;
		}
		//</editor-fold>
	}

}
