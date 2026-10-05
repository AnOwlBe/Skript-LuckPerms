package owlbe.skriptLuckPerms.luckperms.wrapper;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper for cases where a mutable permission node is needed.
 */
public class PermissionNodeWrapper extends NodeWrapper {

	private final String key;

	public PermissionNodeWrapper(String key) {
		this.key = key;
	}

	public @NotNull String key() { return this.key; }

	public @NotNull String permission() {
		return this.key;
	}

	@Override
	public @NotNull PermissionNode build() {
		PermissionNode.Builder builder = PermissionNode.builder(key);

		builder.value(value);

		if (context != null)
			builder.context(context);

		if (expiry != null)
			builder.expiry(expiry.getAs(Timespan.TimePeriod.MILLISECOND), TimeUnit.MILLISECONDS);

		this.built = true;
		return builder.build();
	}

	@Override
	public NodeType<?> type() {
		return NodeType.PERMISSION;
	}

}
