package owlbe.skriptLuckPerms.modules.node.permission;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.yggdrasil.Fields;
import net.luckperms.api.node.types.PermissionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.modules.node.NodeUtils;
import owlbe.skriptLuckPerms.utils.TimeUtils;

import java.io.StreamCorruptedException;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;

@SuppressWarnings("UnstableApiUsage")
public class PermissionClassInfo extends ClassInfo<PermissionNode> {

	public PermissionClassInfo() {
		super(PermissionNode.class, "luckpermspermission");
		this.user("luckperms ?permissions?")
				.name("LuckPerms Permission")
				.description("Represents a LuckPerms permission.")
				.since("INSERT VERSION")
				.parser(new PermissionParser())
				.serializer(new PermissionSerializer())
				.property(TYPED_VALUE,
						"The value of this permission.",
						SkriptLuckPerms.getAddonInstance(),
						new PermissionValueHandler());
	}

	private static class PermissionParser extends Parser<PermissionNode> {
		//<editor-fold desc="permission parser" defaultstate="collapsed">
		@Override
		public @Nullable PermissionNode parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(PermissionNode node, int flags) {
			String permission = node.getPermission();
			if (node.getExpiry() != null)
				return "permission node with permission '" + permission + "' and expiry " + TimeUtils.fromInstant(node.getExpiry());

			return "permission node with permission '" + permission + "'";
		}

		@Override
		public String toVariableNameString(PermissionNode permission) {
			return permission.getKey();
		}

		//</editor-fold>
	}

	private static class PermissionSerializer extends Serializer<PermissionNode> {
		//<editor-fold desc="permission node serializer" defaultstate="collapsed">
		@Override
		public Fields serialize(PermissionNode node) {
			return NodeUtils.serialize(node);
		}

		@Override
		public void deserialize(PermissionNode node, Fields fields) {
			assert false;
		}

		@Override
		protected PermissionNode deserialize(Fields fields) throws StreamCorruptedException {
			return NodeUtils.deserialize(fields) instanceof PermissionNode node ? node : null;
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

	private static class PermissionValueHandler implements TypedValueHandler<PermissionNode, String> {
		//<editor-fold desc="permission node value handler" defaultstate="collapsed">


		@Override
		public @Nullable String convert(PermissionNode node) {
			return node.getPermission();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

}
