package owlbe.skriptLuckPerms.modules.node.chatmeta;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.node.metadata.types.InheritanceOriginMetadata;
import net.luckperms.api.node.types.ChatMetaNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import owlbe.skriptLuckPerms.modules.node.chatmeta.elements.expressions.ExprSecCreateChatMeta.ChatMetaSectionEvent;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;
import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.*;

@SuppressWarnings({"UnstableApiUsage", "unchecked", "rawtypes"})
public class ChatMetaClassInfo extends ClassInfo<ChatMetaNode> {

	public ChatMetaClassInfo() {
		super(ChatMetaNode.class, "luckpermschatmeta");
		this.user("luckperms ?chatmetas?")
				.name("LuckPerms Chat Meta")
				.description("Represents a LuckPerms chat meta.")
				.since("1.0")
				.parser(new ChatMetaParser())
				.defaultExpression(new EventValueExpression<>(ChatMetaNode.class))
				.property(getProperty(PRIORITY),
						"The priority of this chat meta.",
						addon,
						new ChatMetaPriorityHandler())
				.property(getProperty(SOURCE),
						"The source of this chat meta.",
						addon,
						new ChatMetaSourceHandler())
				.property(getProperty(TYPED_VALUE),
						"The value of this chat meta.",
						addon,
						new ChatMetaValueHandler());
	}

	private static class ChatMetaParser extends Parser<ChatMetaNode> {
		//<editor-fold desc="chat meta parser" defaultstate="collapsed">
		@Override
		@Nullable
		public ChatMetaNode parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(ChatMetaNode node, int i) {
			return node.getMetaValue();
		}

		@Override
		public String toVariableNameString(ChatMetaNode node) {
			return node.getMetaValue();
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
		public Class<?>[] acceptChange(ChangeMode mode) {
			if (!(ParserInstance.get().isCurrentEvent(ChatMetaSectionEvent.class))) {
				Skript.error("You can only change the priority of a chat meta node in a 'chat meta' section.");
				return null;
			}

			return switch (mode) {
				case SET, ADD, REMOVE, RESET -> CollectionUtils.array(Integer.class);
				default -> null;
			};
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
