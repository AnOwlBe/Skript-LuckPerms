package owlbe.skriptLuckPerms.modules.node.meta;

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
import owlbe.skriptLuckPerms.utils.wrapper.MetaNodeWrapper;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;
import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.EXPIRY;
import static owlbe.skriptLuckPerms.skript.properties.Properties.getProperty;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class MetaWrapperClassInfo extends ClassInfo<MetaNodeWrapper> {

	public MetaWrapperClassInfo() {
		super(MetaNodeWrapper.class, "luckpermsmetawrapper");
		this.user("luckperms meta ?wrappers?")
				.name(NO_DOC)
				.description(NO_DOC)
				.since("INSERT VERSION")
				.defaultExpression(new EventValueExpression<>(MetaNodeWrapper.class))
				.property(TYPED_VALUE,
						"The value of this meta.",
						addon,
						new MetaValueHandler())
				.property(getProperty(EXPIRY),
						"The expiry of this meta.",
						addon,
						new MetaExpiryHandler());
	}


	private static class MetaValueHandler implements TypedValueHandler<MetaNodeWrapper, String> {
		//<editor-fold desc="meta node value handler" defaultstate="collapsed">

		private boolean shouldError = false;
		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable String convert(MetaNodeWrapper node) {
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
		public void change(MetaNodeWrapper node, Object[] delta, ChangeMode mode) {
			if (shouldError && producer != null) {
				producer.error("You can only change the value of a meta node inside a 'meta' section.");
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

	private static class MetaExpiryHandler implements ExpressionPropertyHandler<MetaNodeWrapper, Timespan> {
		//<editor-fold desc="meta node expiry handler" defaultstate="collapsed">

		private boolean shouldError = false;
		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable Timespan convert(MetaNodeWrapper node) {
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
		public void change(MetaNodeWrapper node, Object[] delta, ChangeMode mode) {
			if (shouldError && producer != null) {
				producer.error("You can only change the expiry of a meta node inside a 'meta' section.");
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
