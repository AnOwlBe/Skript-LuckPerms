package owlbe.skriptLuckPerms.luckperms.wrapper;

import ch.njol.skript.util.Timespan;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper for cases where a mutable node is needed.
 */
public abstract class NodeWrapper {

	public @Nullable Timespan expiry;
	public @Nullable ContextSet context;
	public boolean value = true;
	protected boolean built = false;

	/**
	 * @return The expiry of this node, or null if none
	 */
	public @Nullable Timespan expiry() {
		return this.expiry;
	}

	/**
	 * The {@link Timespan} for when this node will expire.
	 * @param expiry When this node will expire
	 */
	public void expiry(@Nullable Timespan expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return The context for this node
	 */
	public @Nullable ContextSet context() {
		return this.context;
	}

	/**
	 * The context required for this node to apply.
	 * @param context The {@link ContextSet} for this node to apply
	 */
	public void context(@Nullable ContextSet context) {
		this.context = context;
	}

	/**
	 * The value of this node (whether it is negated or not.
	 * @param value The value to set
	 */
	public void value(boolean value) {
		this.value = value;
	}

	/**
	 * The value of this node (whether it is negated or not).
	 * @return The value
	 */
	public boolean value() {
		return this.value;
	}

	/**
	 * Builds the wrapped node using the given values and the returns respective {@link Node}.
	 * @return The built {@link Node}
	 */
	public abstract @NotNull Node build();

	/**
	 * @return Whether this node has been built yet.
	 */
	public boolean built() {
		return this.built;
	}

	/**
	 * @return The type of this node
	 */
	public abstract NodeType<?> type();


}
