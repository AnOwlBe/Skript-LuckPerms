package owlbe.skriptLuckPerms.utils.wrapper;

import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.Timespan.TimePeriod;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.node.ChatMetaType;
import net.luckperms.api.node.types.ChatMetaNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper for cases where a mutable chat meta is needed.
 */
public class ChatMetaNodeWrapper {

	private final ChatMetaType type;
	private String value;
	private int priority = 0;
	private @Nullable Timespan expiry;
	private @Nullable ContextSet context;

	public ChatMetaNodeWrapper(ChatMetaType type) {
		this.type = type;
	}

	public @NotNull String getValue() { return value; }

	public void setValue(@NotNull String value) { this.value = value; }

	public int getPriority() { return priority; }

	public void setPriority(int priority) { this.priority = priority; }

	public @Nullable Timespan getExpiry() { return expiry; }

	public void setExpiry(@Nullable Timespan expiry) { this.expiry = expiry; }

	public @Nullable ContextSet getContext() {
		return this.context;
	}

	public void setContext(@Nullable ContextSet context) {
		this.context = context;
	}

	@SuppressWarnings("rawtypes")
	public @Nullable ChatMetaNode build() {
		ChatMetaNode.Builder<?, ?> builder = type.builder(value, priority);

		if (expiry != null)
			builder.expiry(expiry.getAs(TimePeriod.MILLISECOND), TimeUnit.MILLISECONDS);

		return builder.build();
	}

}
