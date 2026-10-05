package owlbe.skriptLuckPerms.modules.node;

import ch.njol.skript.util.Timespan;
import ch.njol.yggdrasil.Fields;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeBuilder;
import net.luckperms.api.node.types.*;
import owlbe.skriptLuckPerms.utils.TimeUtils;

import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class NodeUtils {

	private NodeUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	/**
	 * Serializes any LuckPerms node.
	 * @param node The node to serialize
	 * @return Fields containing the serialized information
	 */
	public static Fields serialize(Node node) {
		//<editor-fold desc="node serializer" defaultstate="collapsed">
		Fields fields = new Fields();

		fields.putObject("value", node.getValue());
		fields.putObject("type", node.getType().name());
		if (node.getExpiry() != null)
			fields.putObject("expiry", TimeUtils.fromInstant(node.getExpiry()).toString());

		Map<String, Set<String>> plainContext = new HashMap<>();

		node.getContexts().toMap().forEach((key, values) ->
				plainContext.put(key, new HashSet<>(values)));

		fields.putObject("context", plainContext);

		switch (node) {
			case ChatMetaNode<?, ?> chatMetaNode -> {
				fields.putObject("metaValue", chatMetaNode.getMetaValue());
				fields.putObject("priority", chatMetaNode.getPriority());
			}
			case InheritanceNode inheritanceNode -> fields.putObject("group", inheritanceNode.getGroupName());
			case MetaNode metaNode -> {
				fields.putObject("metaKey", metaNode.getMetaKey());
				fields.putObject("metaValue", metaNode.getMetaValue());
			}
			case PermissionNode permissionNode -> fields.putObject("permission", permissionNode.getPermission());
			default -> fields.putObject("key", node.getKey());
		}

		return fields;
		//</editor-fold>
	}

	/**
	 * Deserializes a LuckPerms node from the given fields.
	 * @param fields The fields to get the data from
	 * @return The deserialized node
	 * @throws StreamCorruptedException If any of the required values are null
	 */
	public static Node deserialize(Fields fields) throws StreamCorruptedException {
		//<editor-fold desc="node deserializer" defaultstate="collapsed">
		String type = fields.getObject("type", String.class);
		if (type == null)
			throw new StreamCorruptedException();

		Boolean value = fields.getObject("value", Boolean.class);
		if (value == null)
			throw new StreamCorruptedException();

		Timespan expiry = fields.getObject("expiry", Timespan.class);

		@SuppressWarnings("unchecked")
		Map<String, Set<String>> context = fields.getObject("context", Map.class);

		NodeBuilder<?, ?> builder = switch (type) {
			case "PREFIX" -> {
				String metaValue = fields.getObject("metaValue", String.class);
				Integer priority = fields.getObject("priority", Integer.class);
				if (metaValue == null || priority == null)
					throw new StreamCorruptedException();
				yield PrefixNode.builder(metaValue, priority);
			}
			case "SUFFIX" -> {
				String metaValue = fields.getObject("metaValue", String.class);
				Integer priority = fields.getObject("priority", Integer.class);
				if (metaValue == null || priority == null)
					throw new StreamCorruptedException();
				yield SuffixNode.builder(metaValue, priority);
			}
			case "META" -> {
				String metaKey = fields.getObject("metaKey", String.class);
				String metaValue = fields.getObject("metaValue", String.class);
				if (metaKey == null || metaValue == null)
					throw new StreamCorruptedException();
				yield MetaNode.builder(metaKey, metaValue);
			}
			case "INHERITANCE" -> {
				String group = fields.getObject("group", String.class);
				if (group == null)
					throw new StreamCorruptedException();
				yield InheritanceNode.builder(group);
			}
			case "PERMISSION" -> {
				String permission = fields.getObject("permission", String.class);
				if (permission == null)
					throw new StreamCorruptedException();
				yield PermissionNode.builder(permission);
			}
			case "NODE" -> {
				String key = fields.getObject("key", String.class);
				if (key == null)
					throw new StreamCorruptedException();

				yield Node.builder(key);
			}
			default -> throw new StreamCorruptedException();
		};

		builder.value(value);

		if (expiry != null)
			builder.expiry(TimeUtils.toInstant(expiry));

		if (context != null) {
			context.forEach((contextKey, values) ->
					values.forEach(contextValue -> builder.withContext(contextKey, contextValue)));
		}

		return builder.build();
		//</editor-fold>
	}

}
