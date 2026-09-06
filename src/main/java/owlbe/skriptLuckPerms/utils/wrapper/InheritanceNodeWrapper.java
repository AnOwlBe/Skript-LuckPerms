package owlbe.skriptLuckPerms.utils.wrapper;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.types.InheritanceNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class InheritanceNodeWrapper {

	private @Nullable Timespan expiry;
	private final @NotNull Group group;
	private @Nullable ContextSet context;

	public InheritanceNodeWrapper(@NotNull Group group) {
		this.group = group;
	}

	public @Nullable ContextSet getContext() {
		return this.context;
	}

	public void setContext(@Nullable ContextSet context) {
		this.context = context;
	}

	public @Nullable Timespan getExpiry() {
		return this.expiry;
	}

	public void setExpiry(@Nullable Timespan expiry) {
		this.expiry = expiry;
	}

	public @NotNull InheritanceNode build() {
		InheritanceNode.Builder builder = InheritanceNode.builder(group);

		if (expiry != null)
			builder.expiry(expiry.getAs(Timespan.TimePeriod.MILLISECOND), TimeUnit.MILLISECONDS);

		return builder.build();
	}

}
