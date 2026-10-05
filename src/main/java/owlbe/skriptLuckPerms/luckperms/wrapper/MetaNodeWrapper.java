package owlbe.skriptLuckPerms.luckperms.wrapper;

import ch.njol.skript.util.Timespan.TimePeriod;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper for cases where a mutable meta node is needed.
 */
public class MetaNodeWrapper extends NodeWrapper {

	private final String metaKey;
	private final String metaValue;

	public MetaNodeWrapper(String metaKey, String metaValue) {
		this.metaKey = metaKey;
		this.metaValue = metaValue;
	}

	public String metaValue() {
		return this.metaValue;
	}

	public String metaKey() {
		return this.metaKey;
	}

	@Override
	public @NotNull MetaNode build() {
		MetaNode.Builder builder = MetaNode.builder(metaKey, metaValue);

		builder.value(value);

		if (context != null)
			builder.context(context);

		if (expiry != null)
			builder.expiry(expiry.getAs(TimePeriod.MILLISECOND), TimeUnit.MILLISECONDS);

		this.built = true;
		return builder.build();
	}

	@Override
	public NodeType<?> type() {
		return NodeType.META;
	}

}
