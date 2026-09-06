package owlbe.skriptLuckPerms.modules.node.chatmeta;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.Timespan;
import ch.njol.util.coll.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.log.runtime.RuntimeErrorProducer;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;
import owlbe.skriptLuckPerms.utils.wrapper.ChatMetaNodeWrapper;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;
import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.*;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class ChatMetaWrapperClassInfo extends ClassInfo<ChatMetaNodeWrapper> {

	public ChatMetaWrapperClassInfo() {
		super(ChatMetaNodeWrapper.class, "luckpermschatmetawrapper");
		this.user("luckperms chatmeta ?wrappers?")
				.name(NO_DOC)
				.description(NO_DOC)
				.since("INSERT VERSION")
				.defaultExpression(new EventValueExpression<>(ChatMetaNodeWrapper.class))
				.property(getProperty(PRIORITY),
						"The priority of this chat meta.",
						addon,
						new ChatMetaPriorityHandler())
				.property(TYPED_VALUE,
						"The value of this chat meta.",
						addon,
						new ChatMetaValueHandler())
				.property(getProperty(EXPIRY),
						"The expiry of this chat meta.",
						addon,
						new ChatMetaExpiryHandler());
	}

	private static class ChatMetaPriorityHandler implements ExpressionPropertyHandler<ChatMetaNodeWrapper, Integer>{
		//<editor-fold desc="chat meta priority handler" defaultstate="collapsed">

		private boolean shouldError = false;
		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable Integer convert(ChatMetaNodeWrapper node) {
			return node.getPriority();
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
		public void change(ChatMetaNodeWrapper node, Object[] delta, ChangeMode mode) {
			if (shouldError && producer != null) {
				producer.error("You can only change the priority of a chat meta node inside a 'chat meta' section.");
				return;
			}

			int amount = delta != null ? (Integer) delta[0] : 0;
			int priority = node.getPriority();

			int newPriority = switch (mode) {
				case SET -> amount;
				case ADD -> priority + amount;
				case REMOVE -> priority - amount;
				default -> priority;
			};

			node.setPriority(newPriority);

		}

		@Override
		public @NotNull Class<Integer> returnType() {
			return Integer.class;
		}
		//</editor-fold>
	}

	private static class ChatMetaValueHandler implements TypedValueHandler<ChatMetaNodeWrapper, String> {
		//<editor-fold desc="chat meta node value handler" defaultstate="collapsed">

		private boolean shouldError = false;
		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable String convert(ChatMetaNodeWrapper node) {
			return node.getValue();
		}

		@Override
		public Class<?>[] acceptChange(ChangeMode mode) {
			if (!(ParserInstance.get().isCurrentEvent(HolderSectionEvent.class)))
				shouldError = true;

			return switch (mode) {
				case SET, RESET -> CollectionUtils.array(String.class);
				default -> null;
			};
		}

		@Override
		public void change(ChatMetaNodeWrapper node, Object[] delta, ChangeMode mode) {
			if (shouldError && producer != null) {
				producer.error("You can only change the value of a chat meta node inside a 'chat meta' section.");
				return;
			}

			if (delta != null)
				node.setValue((String) delta[0]);
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

	private static class ChatMetaExpiryHandler implements ExpressionPropertyHandler<ChatMetaNodeWrapper, Timespan> {
		//<editor-fold desc="chat meta node expiry handler" defaultstate="collapsed">

		private boolean shouldError = false;
		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable Timespan convert(ChatMetaNodeWrapper node) {
			return node.getExpiry();
		}

		@Override
		public Class<?>[] acceptChange(ChangeMode mode) {
			if (!(ParserInstance.get().isCurrentEvent(HolderSectionEvent.class)))
				shouldError = true;

			return switch (mode) {
				case SET, RESET -> CollectionUtils.array(Timespan.class);
				default -> null;
			};
		}

		@Override
		public void change(ChatMetaNodeWrapper node, Object[] delta, ChangeMode mode) {
			if (shouldError && producer != null) {
				producer.error("You can only change the expiry of a chat meta node inside a 'chat meta' section.");
				return;
			}

			node.setExpiry(delta != null ? (Timespan) delta[0] : null);
		}

		@Override
		public @NotNull Class<Timespan> returnType() {
			return Timespan.class;
		}
		//</editor-fold>
	}

}
