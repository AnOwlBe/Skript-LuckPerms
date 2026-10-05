package owlbe.skriptLuckPerms.modules.node.chatmeta;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.yggdrasil.Fields;
import net.luckperms.api.node.ChatMetaType;
import net.luckperms.api.node.metadata.types.InheritanceOriginMetadata;
import net.luckperms.api.node.types.ChatMetaNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.modules.node.NodeUtils;
import owlbe.skriptLuckPerms.modules.node.chatmeta.elements.expressions.ExprSecCreateChatMeta.ChatMetaSectionEvent;
import owlbe.skriptLuckPerms.utils.TimeUtils;

import java.io.StreamCorruptedException;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;
import static owlbe.skriptLuckPerms.skript.properties.Properties.*;
import static owlbe.skriptLuckPerms.utils.PropertyUtils.getProperty;

@SuppressWarnings({"UnstableApiUsage", "unchecked", "rawtypes"})
public class ChatMetaClassInfo extends ClassInfo<ChatMetaNode> {

	public ChatMetaClassInfo() {
		super(ChatMetaNode.class, "luckpermschatmeta");
		this.user("luckperms ?chatmetas?")
				.name("LuckPerms Chat Meta")
				.description("Represents a LuckPerms chat meta node.")
				.since("1.0")
				.parser(new ChatMetaParser())
				.serializer(new ChatMetaSerializer())
				.defaultExpression(new EventValueExpression<>(ChatMetaNode.class))
				.property(getProperty(PRIORITY, ExpressionPropertyHandler.class),
						"The priority of this chat meta.",
						SkriptLuckPerms.getAddonInstance(),
						new ChatMetaPriorityHandler())
				.property(getProperty(SOURCE, ExpressionPropertyHandler.class),
						"The source of this chat meta.",
						SkriptLuckPerms.getAddonInstance(),
						new ChatMetaSourceHandler())
				.property(getProperty(TYPED_VALUE, ExpressionPropertyHandler.class),
						"The value of this chat meta.",
						SkriptLuckPerms.getAddonInstance(),
						new ChatMetaValueHandler());
	}

	private static class ChatMetaParser extends Parser<ChatMetaNode> {
		//<editor-fold desc="chat meta parser" defaultstate="collapsed">
		@Override
		public @Nullable ChatMetaNode parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(ChatMetaNode node, int flags) {
			Object type = node.getMetaType() == ChatMetaType.PREFIX ? ChatMetaType.PREFIX : ChatMetaType.SUFFIX;
			type = type.toString().toLowerCase();

			if (node.getExpiry() != null)
				return type + " node with value '" + node.getMetaValue() + "' and expiry " + TimeUtils.fromInstant(node.getExpiry());

			return type + " node with value '" + node.getMetaValue() + "'";
		}

		@Override
		public String toVariableNameString(ChatMetaNode node) {
			return node.getMetaValue();
		}
		//</editor-fold>
	}

	private static class ChatMetaSerializer extends Serializer<ChatMetaNode> {
		//<editor-fold desc="chat meta node serializer" defaultstate="collapsed">
		@Override
		public Fields serialize(ChatMetaNode node) {
			return NodeUtils.serialize(node);
		}

		@Override
		public void deserialize(ChatMetaNode node, Fields fields) {
			assert false;
		}

		@Override
		protected ChatMetaNode deserialize(Fields fields) throws StreamCorruptedException {
			return NodeUtils.deserialize(fields) instanceof ChatMetaNode node ? node : null;
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

	private static class ChatMetaPriorityHandler implements ExpressionPropertyHandler<ChatMetaNode, Integer>{
		//<editor-fold desc="chat meta priority handler" defaultstate="collapsed">
		@Override
		public @Nullable Integer convert(ChatMetaNode node) {
			return node.getPriority();
		}

		@Override
		public @NotNull Class<Integer> returnType() {
			return Integer.class;
		}
		//</editor-fold>
	}

	private static class ChatMetaSourceHandler implements ExpressionPropertyHandler<ChatMetaNode, String>{
		//<editor-fold desc="chat meta source handler" defaultstate="collapsed">
		@Override
		public @Nullable String convert(ChatMetaNode node) {
			InheritanceOriginMetadata origin = node.metadata(InheritanceOriginMetadata.KEY);
			return origin.getOrigin().getName();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

	private static class ChatMetaValueHandler implements TypedValueHandler<ChatMetaNode, String> {
		//<editor-fold desc="chat meta node value handler" defaultstate="collapsed">

		@Override
		public @Nullable String convert(ChatMetaNode node) {
			return node.getMetaValue();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

}
