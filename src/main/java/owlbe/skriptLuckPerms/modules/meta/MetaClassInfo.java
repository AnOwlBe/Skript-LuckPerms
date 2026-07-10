package owlbe.skriptLuckPerms.modules.meta;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import net.luckperms.api.node.metadata.types.InheritanceOriginMetadata;
import net.luckperms.api.node.types.ChatMetaNode;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;

import javax.annotation.Nullable;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.*;

@SuppressWarnings({"UnstableApiUsage", "unchecked", "rawtypes"})
public class MetaClassInfo extends ClassInfo<ChatMetaNode> {

	public MetaClassInfo() {
		super(ChatMetaNode.class, "luckpermschatmeta");
		this.user("luckperms ?chatmetas?")
				.name("Chat Meta")
				.description("Represents a LuckPerms chat meta.")
				.since("1.0")
				.parser(new ChatMetaParser())
				.defaultExpression(new EventValueExpression<>(ChatMetaNode.class))
				.property(getProperty(PRIORITY),
						"The priority of a chat meta.",
						addon,
						new ChatMetaPriorityHandler())
				.property(getProperty(SOURCE),
						"The source of a chat meta.",
						addon,
						new ChatMetaSourceHandler());
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

}
