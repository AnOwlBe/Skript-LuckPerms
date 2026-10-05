package owlbe.skriptLuckPerms.modules.node;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.Timespan;
import ch.njol.yggdrasil.Fields;
import net.luckperms.api.context.Context;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;

import java.io.StreamCorruptedException;
import java.time.Duration;

import static owlbe.skriptLuckPerms.skript.properties.Properties.*;
import static owlbe.skriptLuckPerms.utils.PropertyUtils.getProperty;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class NodeClassInfo extends ClassInfo<Node> {

	public NodeClassInfo() {
		super(Node.class, "luckpermsnode");
		this.user("luckperms ?nodes?")
				.name("LuckPerms Node")
				.description("Represents a LuckPerms node.")
				.since("INSERT VERSION")
				.parser(new NodeParser())
				.serializer(new NodeSerializer())
				.property(getProperty(EXPIRY, ExpressionPropertyHandler.class),
						"The expiry of this node.",
						SkriptLuckPerms.getAddonInstance(),
						new NodeExpiryHandler())
				.property(getProperty(CONTEXT, ExpressionPropertyHandler.class),
						"The context of this node.",
						SkriptLuckPerms.getAddonInstance(),
						new NodeContextHandler());
	}

	private static class NodeParser extends Parser<Node> {
		//<editor-fold desc="node parser" defaultstate="collapsed">
		@Override
		public @Nullable Node parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(Node node, int flags) {
			Duration duration = node.getExpiryDuration();
			String key = node.getKey();
			ImmutableContextSet context = node.getContexts();

			if (duration == null || duration.toMillis() == 0)
				return "node '" + key + "' with context " + context;

			Timespan timespan = new Timespan(duration.toMillis());
			return "node '" + key + "' with duration " + timespan + " with context " + context;
		}

		@Override
		public String toVariableNameString(Node node) {
			return node.getKey();
		}

		//</editor-fold>
	}

	private static class NodeSerializer extends Serializer<Node> {
		//<editor-fold desc="node serializer" defaultstate="collapsed">
		@Override
		public Fields serialize(Node node) {
			return NodeUtils.serialize(node);
		}

		@Override
		public void deserialize(Node node, Fields fields) {
			assert false;
		}

		@Override
		protected Node deserialize(Fields fields) throws StreamCorruptedException {
			return NodeUtils.deserialize(fields);
		}

		@Override
		public boolean mustSyncDeserialization() {
			return true;
		}

		@Override
		public boolean canBeInstantiated() {
			return false;
		}
		//</editor-fold>
	}

	private static class NodeExpiryHandler implements ExpressionPropertyHandler<Node, Timespan> {
		//<editor-fold desc="node expiry handler" defaultstate="collapsed">

		@Override
		public @Nullable Timespan convert(Node node) {
			Duration expiryDuration = node.getExpiryDuration();
			if (expiryDuration == null)
				return new Timespan(0);
			return new Timespan(expiryDuration.toMillis());
		}

		@Override
		public @NotNull Class<Timespan> returnType() {
			return Timespan.class;
		}
		//</editor-fold>
	}

	private static class NodeContextHandler implements ExpressionPropertyHandler<Node, Object> {
		//<editor-fold desc="node context handler" defaultstate="collapsed">

		@Override
		public @Nullable Context[] convert(Node node) {
			return node.getContexts().toSet().toArray(new Context[0]);
		}

		@Override
		@SuppressWarnings("rawtypes")
		public @NotNull Class<Object> returnType() {
			return (Class<Object>) (Class) Context.class;
		}
		//</editor-fold>
	}

}
