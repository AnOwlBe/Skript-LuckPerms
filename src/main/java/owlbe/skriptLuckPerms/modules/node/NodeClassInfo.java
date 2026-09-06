package owlbe.skriptLuckPerms.modules.node;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.Timespan;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;

import java.time.Duration;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.EXPIRY;
import static owlbe.skriptLuckPerms.skript.properties.Properties.getProperty;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class NodeClassInfo extends ClassInfo<Node> {

	public NodeClassInfo() {
		super(Node.class, "luckpermsnode");
		this.user("luckperms ?nodes?")
				.name("LuckPerms Node")
				.description("Represents a LuckPerms node.")
				.since("INSERT VERSION")
				.parser(new NodeParser())
				.property(getProperty(EXPIRY),
						"The expiry of this node.",
						addon,
						new NodeExpiryHandler());
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

}
