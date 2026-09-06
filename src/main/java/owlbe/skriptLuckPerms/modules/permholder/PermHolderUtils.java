package owlbe.skriptLuckPerms.modules.permholder;

import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public final class PermHolderUtils {

	private PermHolderUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	public static @Nullable ChatMetaNode<?, ?> getPrimaryPrefix(PermissionHolder holder) {
		return holder.getNodes(NodeType.PREFIX).stream()
				.max(Comparator.comparingInt(PrefixNode::getPriority))
				.orElse(null);
	}

	public static @Nullable ChatMetaNode<?, ?> getPrimarySuffix(PermissionHolder holder) {
		return holder.getNodes(NodeType.SUFFIX).stream()
				.max(Comparator.comparingInt(SuffixNode::getPriority))
				.orElse(null);
	}

	public static @NotNull ChatMetaNode<?, ?>[] getPrefixes(PermissionHolder holder) {
		return holder.getNodes(NodeType.PREFIX).toArray(ChatMetaNode[]::new);
	}

	public static @NotNull ChatMetaNode<?, ?>[] getSuffixes(PermissionHolder holder) {
		return holder.getNodes(NodeType.SUFFIX).toArray(ChatMetaNode[]::new);
	}

	public static boolean hasNode(PermissionHolder holder, NodeType<?> type, Node node) {
		return holder.getNodes(type).stream()
				.anyMatch(node::equals);
	}

	public static Node[] getNodes(PermissionHolder holder, NodeType<?> type) {
		return holder.getNodes(type).toArray(Node[]::new);
	}

}
