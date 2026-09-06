package owlbe.skriptLuckPerms.modules.permholder.user;

import ch.njol.skript.lang.Expression;
import net.luckperms.api.model.user.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder.HolderSectionEvent;


public final class UserUtils {

	private UserUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}

	/**
	 * Gets the user from the given event or expression.
	 * If the expression is null, the user is retrieved from the {@link HolderSectionEvent}.
	 * @param event the event to get the user from
	 * @param user the user expression, or null to use the event user
	 * @return the user, or null.
	 */
	public static @Nullable User getUser(Event event, @Nullable Expression<User> user) {
		if (user == null && event instanceof HolderSectionEvent holderEvent)
			return holderEvent.getHolder() instanceof User holderUser ? holderUser : null;
		if (user == null)
			return null; // won't ever be null but to shut up IntelliJ
		return user.getSingle(event);
	}
}
