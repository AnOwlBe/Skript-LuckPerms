package owlbe.skriptLuckPerms.utils.wrapper;

import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.Timespan.TimePeriod;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.node.types.MetaNode;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper for cases where a mutable meta is needed.
 */
public class MetaNodeWrapper {

	private String key;
	private String value;
	private @Nullable Timespan expiry;
	private @Nullable ContextSet context;

	public MetaNodeWrapper(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() { return value; }

	public void setValue(String value) {
		this.value = value;
	}

	public @Nullable Timespan getExpiry() { return expiry; }

	public void setExpiry(@Nullable Timespan expiry) { this.expiry = expiry; }

	public @Nullable ContextSet getContext() {
		return this.context;
	}

	public void setContext(@Nullable ContextSet context) {
		this.context = context;
	}

	public @Nullable MetaNode build() {
		MetaNode.Builder builder = MetaNode.builder(key, value);

		if (context != null)
			builder.context(context);

		if (expiry != null)
			builder.expiry(expiry.getAs(TimePeriod.MILLISECOND), TimeUnit.MILLISECONDS);

		return builder.build();
	}

}
