package owlbe.skriptLuckPerms.modules.node.meta;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.Timespan;
import net.luckperms.api.node.types.MetaNode;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;

import javax.annotation.Nullable;
import java.time.Duration;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;
import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.getProperty;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class MetaNodeClassInfo extends ClassInfo<MetaNode> {

	public MetaNodeClassInfo() {
		super(MetaNode.class, "luckpermsmeta");
		this.user("luckperms ?metas?")
				.name("LuckPerms Meta Node")
				.description("Represents a LuckPerms meta node.")
				.since("INSERT VERSION")
				.parser(new MetaNodeParser())
				.property(getProperty(TYPED_VALUE),
						"The value of this meta node.",
						addon,
						new MetaNodeValueHandler()
				);
	}

	private static class MetaNodeParser extends Parser<MetaNode> {
		//<editor-fold desc="meta node parser" defaultstate="collapsed">
		@Override
		@Nullable
		public MetaNode parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(MetaNode node, int flags) {
			Duration duration = node.getExpiryDuration();
			String key = node.getKey();
			if (duration == null || duration.toMillis() == 0)
				return "meta node with key '" + key + "' and value" + node.getMetaValue();

			Timespan timespan = new Timespan(duration.toMillis());
			return "meta node with key '" + key + "' and value" + node.getMetaValue() + "and duration" + timespan;
		}

		@Override
		public String toVariableNameString(MetaNode node) {
			return node.getKey();
		}

		//</editor-fold>
	}

	private static class MetaNodeValueHandler implements TypedValueHandler<MetaNode, String> {
		//<editor-fold desc="meta node value handler" defaultstate="collapsed">

		@Override
		public @Nullable String convert(MetaNode node) {
			return node.getMetaValue();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

}
