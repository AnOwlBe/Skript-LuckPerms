package owlbe.skriptLuckPerms.luckperms.wrapper;

import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.jetbrains.annotations.NotNull;
import owlbe.skriptLuckPerms.utils.TimeUtils;

/**
 * A wrapper for cases where a mutable inheritance node is needed.
 */
public class InheritanceNodeWrapper extends NodeWrapper {

	private final @NotNull Group group;

	public InheritanceNodeWrapper(@NotNull Group group) {
		this.group = group;
	}

	/**
	 * @return The {@link Group} of this inheritance node
	 */
	public @NotNull Group group() {
		return this.group;
	}

	@Override
	public @NotNull InheritanceNode build() {
		InheritanceNode.Builder builder = InheritanceNode.builder(group);

		builder.value(value);

		if (context != null)
			builder.context(context);

		if (expiry != null)
			builder.expiry(TimeUtils.toInstant(expiry));

		this.built = true;
		return builder.build();
	}

	@Override
	public NodeType<?> type() {
		return NodeType.INHERITANCE;
	}

}
