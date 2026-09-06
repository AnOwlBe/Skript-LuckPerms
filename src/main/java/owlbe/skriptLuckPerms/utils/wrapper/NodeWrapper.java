package owlbe.skriptLuckPerms.utils.wrapper;

import ch.njol.skript.util.Timespan;
import org.jetbrains.annotations.Nullable;

public class NodeWrapper {

	private @Nullable Timespan expiry;

	public NodeWrapper(@Nullable Timespan expiry) {
		this.expiry = expiry;
	}

	public @Nullable Timespan getExpiry() {
		return this.expiry;
	}

	public void setExpiry(@Nullable Timespan expiry) {
		this.expiry = expiry;
	}


}
