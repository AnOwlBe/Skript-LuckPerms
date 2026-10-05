package owlbe.skriptLuckPerms.modules.node;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.Timespan;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.context.Context;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.log.runtime.RuntimeErrorProducer;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.luckperms.wrapper.NodeWrapper;
import owlbe.skriptLuckPerms.utils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;

import static owlbe.skriptLuckPerms.skript.properties.Properties.CONTEXT;
import static owlbe.skriptLuckPerms.skript.properties.Properties.EXPIRY;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class NodeWrapperClassInfo extends ClassInfo<NodeWrapper> {

	public NodeWrapperClassInfo() {
		super(NodeWrapper.class, "luckpermsnodewrapper");
		this.user("luckperms node ?wrappers?")
				.name(NO_DOC)
				.description(NO_DOC)
				.since("INSERT VERSION")
				.defaultExpression(new EventValueExpression<>(NodeWrapper.class))
				.property(PropertyUtils.getProperty(EXPIRY, ExpressionPropertyHandler.class),
						"The expiry of this node. Can be changed. ",
						SkriptLuckPerms.getAddonInstance(),
						new NodeExpiryHandler())
				.property(PropertyUtils.getProperty(CONTEXT, ExpressionPropertyHandler.class),
						"The context of this node. Can be changed.",
						SkriptLuckPerms.getAddonInstance(),
						new NodeContextHandler());
	}

	private static class NodeExpiryHandler implements ExpressionPropertyHandler<NodeWrapper, Timespan> {
		//<editor-fold desc="node expiry handler" defaultstate="collapsed">

		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable Timespan convert(NodeWrapper node) {
			return node.expiry();
		}

		@Override
		public Class<?>[] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, RESET -> CollectionUtils.array(Timespan.class);
				default -> null;
			};
		}

		@Override
		public void change(NodeWrapper node, Object[] delta, ChangeMode mode) {
			if (node.built() && producer != null) {
				String nodeName = node.type().name().toLowerCase().replace('_', ' ');
				producer.error("You can only change the expiry of a " + nodeName + " node inside a '" + nodeName + "' section.");
				return;
			}

			node.expiry(delta != null ? (Timespan) delta[0] : null);
		}

		@Override
		public @NotNull Class<Timespan> returnType() {
			return Timespan.class;
		}
		//</editor-fold>
	}

	private static class NodeContextHandler implements ExpressionPropertyHandler<NodeWrapper, Object> {
		//<editor-fold desc="node context handler" defaultstate="collapsed">

		private @Nullable RuntimeErrorProducer producer;

		@Override
		public boolean init(Expression<?> parentExpression, ParserInstance parser) {
			if (parentExpression instanceof RuntimeErrorProducer runtimeProducer)
				this.producer = runtimeProducer;

			return true;
		}

		@Override
		public @Nullable Context[] convert(NodeWrapper node) {
			ContextSet context = node.context();

			if (context != null)
				return context.toSet().toArray(new Context[0]);

			return new Context[0];
		}

		@Override
		public Class<?>[] acceptChange(ChangeMode mode) {
			return switch (mode) {
				case SET, ADD, REMOVE, DELETE, RESET -> CollectionUtils.array(Context[].class);
				default -> null;
			};
		}

		@Override
		public void change(NodeWrapper node, Object[] delta, ChangeMode mode) {
			if (node.built() && producer != null) {
				String nodeName = node.type().name().toLowerCase().replace('_', ' ');
				producer.error("You can only change the context of a " + nodeName + " node inside a '" + nodeName + "' section.");
				return;
			}

			List<Context> contexts = new ArrayList<>();
			ContextSet existing = node.context();

			if (existing != null)
				contexts.addAll(existing.toSet());

			switch (mode) {
				case SET -> {
					if (delta == null)
						return;

					contexts.clear();

					for (Object context : delta)
						contexts.add((Context) context);
				}
				case ADD -> {
					if (delta == null)
						return;

					for (Object context : delta)
						contexts.add((Context) context);
				}
				case REMOVE -> {
					if (delta == null)
						return;

					for (Object context : delta)
						contexts.remove((Context) context);
				}
				case DELETE, RESET -> contexts.clear();
			}

			ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
			for (Context context : contexts)
				builder.add(context);

			node.context(builder.build());

		}

		@Override
		@SuppressWarnings("rawtypes")
		public @NotNull Class<Object> returnType() {
			return (Class<Object>) (Class) Context.class;
		}
		//</editor-fold>
	}

}
