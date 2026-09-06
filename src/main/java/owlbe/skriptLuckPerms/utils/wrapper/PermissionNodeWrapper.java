package owlbe.skriptLuckPerms.utils.wrapper;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.node.types.PermissionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper for cases where a mutable permission is needed.
 */
public class PermissionNodeWrapper {

	private final String key;
	private @Nullable Timespan expiry;
	private ContextSet context;

	public PermissionNodeWrapper(String key) {
		this.key = key;
	}

	public @NotNull String getKey() { return key; }

	public @Nullable Timespan getExpiry() { return expiry; }

	public void setExpiry(@Nullable Timespan expiry) { this.expiry = expiry; }

	public @Nullable ContextSet getContext() {
		return this.context;
	}

	public void setContext(ContextSet context) {
		this.context = context;
	}

	public @NotNull PermissionNode build() {
		PermissionNode.Builder builder = PermissionNode.builder(key);

		if (expiry != null)
			builder.expiry(expiry.getAs(Timespan.TimePeriod.MILLISECOND), TimeUnit.MILLISECONDS);

		if (context != null)
			builder.context(context);

		return builder.build();
	}

}
