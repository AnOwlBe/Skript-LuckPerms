package owlbe.skriptLuckPerms.modules.node.inheritance;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.yggdrasil.Fields;
import net.luckperms.api.node.types.InheritanceNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.modules.node.NodeUtils;
import owlbe.skriptLuckPerms.utils.TimeUtils;

import java.io.StreamCorruptedException;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class InheritanceClassInfo extends ClassInfo<InheritanceNode> {

	public InheritanceClassInfo() {
		super(InheritanceNode.class, "luckpermsinheritance");
		this.user("luckperms ?inheritances?")
				.name("LuckPerms Inheritance Node")
				.description("Represents a LuckPerms inheritance node.")
				.since("INSERT VERSION")
				.parser(new InheritanceNodeParser())
				.serializer(new InheritanceSerializer())
				.property(TYPED_VALUE,
						"The value of this inheritance.",
						SkriptLuckPerms.getAddonInstance(),
						new InheritanceValueHandler());
	}

	private static class InheritanceNodeParser extends Parser<InheritanceNode> {
		//<editor-fold desc="inheritance node parser" defaultstate="collapsed">
		@Override
		public @Nullable InheritanceNode parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(InheritanceNode node, int flags) {
			String group = node.getGroupName();
			if (node.getExpiry() != null)
				return "inheritance node with group '" + group + "' and expiry " + TimeUtils.fromInstant(node.getExpiry());

			return "inheritance node with group '" + group + "'";
		}

		@Override
		public String toVariableNameString(InheritanceNode node) {
			return node.getGroupName();
		}

		//</editor-fold>
	}

	private static class InheritanceSerializer extends Serializer<InheritanceNode> {
		//<editor-fold desc="inheritance node serializer" defaultstate="collapsed">
		@Override
		public Fields serialize(InheritanceNode node) {
			return NodeUtils.serialize(node);
		}

		@Override
		public void deserialize(InheritanceNode node, Fields fields) {
			assert false;
		}

		@Override
		protected InheritanceNode deserialize(Fields fields) throws StreamCorruptedException {
			return NodeUtils.deserialize(fields) instanceof InheritanceNode node ? node : null;
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

	private static class InheritanceValueHandler implements TypedValueHandler<InheritanceNode, String> {
		//<editor-fold desc="inheritance node value handler" defaultstate="collapsed">


		@Override
		public @Nullable String convert(InheritanceNode node) {
			return node.getGroupName();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

}
