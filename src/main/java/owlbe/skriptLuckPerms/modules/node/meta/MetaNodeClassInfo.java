package owlbe.skriptLuckPerms.modules.node.meta;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.Timespan;
import ch.njol.yggdrasil.Fields;
import net.luckperms.api.node.types.MetaNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.modules.node.NodeUtils;
import owlbe.skriptLuckPerms.modules.node.meta.elements.expressions.ExprSecCreateMeta.MetaSectionEvent;
import owlbe.skriptLuckPerms.utils.TimeUtils;

import java.io.StreamCorruptedException;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;
import static owlbe.skriptLuckPerms.skript.properties.Properties.EXPIRY;
import static owlbe.skriptLuckPerms.utils.PropertyUtils.getProperty;

@SuppressWarnings({"UnstableApiUsage"})
public class MetaNodeClassInfo extends ClassInfo<MetaNode> {

	public MetaNodeClassInfo() {
		super(MetaNode.class, "luckpermsmeta");
		this.user("luckperms ?metas?")
				.name("LuckPerms Meta Node")
				.description("Represents a LuckPerms meta node.")
				.since("INSERT VERSION")
				.parser(new MetaNodeParser())
				.serializer(new MetaSerializer())
				.property(getProperty(TYPED_VALUE, ExpressionPropertyHandler.class),
						"The value of this meta node.",
						SkriptLuckPerms.getAddonInstance(),
						new MetaNodeValueHandler())
				.property(getProperty(EXPIRY, ExpressionPropertyHandler.class),
						"The expiry of this meta node.",
						SkriptLuckPerms.getAddonInstance(),
						new MetaNodeExpiryHandler());
	}

	private static class MetaNodeParser extends Parser<MetaNode> {
		//<editor-fold desc="meta node parser" defaultstate="collapsed">
		@Override
		public @Nullable MetaNode parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(MetaNode node, int flags) {
			String key = node.getKey();
			String metaValue = node.getMetaValue();

			if (node.getExpiry() != null)
				return "meta node with key '" + key + "' and value '" + metaValue + "' and expiry " + TimeUtils.fromInstant(node.getExpiry());

			return "meta node with key '" + key + "' and value ' " + node.getMetaValue() + "'";
		}

		@Override
		public String toVariableNameString(MetaNode node) {
			return node.getKey();
		}

		//</editor-fold>
	}

	private static class MetaSerializer extends Serializer<MetaNode> {
		//<editor-fold desc="meta node serializer" defaultstate="collapsed">
		@Override
		public Fields serialize(MetaNode node) {
			return NodeUtils.serialize(node);
		}

		@Override
		public void deserialize(MetaNode node, Fields fields) {
			assert false;
		}

		@Override
		protected MetaNode deserialize(Fields fields) throws StreamCorruptedException {
			return NodeUtils.deserialize(fields) instanceof MetaNode node ? node : null;
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

	private static class MetaNodeExpiryHandler implements ExpressionPropertyHandler<MetaNode, Timespan> {
		//<editor-fold desc="meta node expiry handler" defaultstate="collapsed">

		@Override
		public @Nullable Timespan convert(MetaNode node) {
			if (node.getExpiry() != null)
				return TimeUtils.fromInstant(node.getExpiry());

			return null;
		}

		@Override
		public @NotNull Class<Timespan> returnType() {
			return Timespan.class;
		}
		//</editor-fold>
	}

}
