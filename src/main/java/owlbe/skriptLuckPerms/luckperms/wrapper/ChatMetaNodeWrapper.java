package owlbe.skriptLuckPerms.luckperms.wrapper;

import ch.njol.skript.util.Timespan.TimePeriod;
import net.luckperms.api.node.ChatMetaType;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper for cases where a mutable chat meta node is needed.
 */
public class ChatMetaNodeWrapper extends NodeWrapper {

	private final ChatMetaType type;
	private final String metaValue;
	private int priority = 0;

	public ChatMetaNodeWrapper(ChatMetaType type, String metaValue) {
		this.type = type;
		this.metaValue = metaValue;
	}

	public @NotNull String metaValue() { return metaValue; }

	public int priority() { return priority; }

	public void priority(int priority) { this.priority = priority; }

	@Override
	@SuppressWarnings("rawtypes")
	public @NotNull ChatMetaNode build() {
		ChatMetaNode.Builder<?, ?> builder = type.builder(metaValue, priority);

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
		return NodeType.CHAT_META;
	}
}
