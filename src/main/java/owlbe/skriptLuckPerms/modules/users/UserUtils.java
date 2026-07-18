package owlbe.skriptLuckPerms.modules.users;

import ch.njol.skript.lang.Expression;
import net.luckperms.api.model.user.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser.UserEvent;

public class UserUtils {

	/**
	 * Gets the user from the given event or expression.
	 * If the expression is null, the user is retrieved from the {@link UserEvent}.
	 * @param event the event to get the user from
	 * @param user the user expression, or null to use the event user
	 * @return the user, or null.
	 */
	public static @Nullable User getUser(Event event, @Nullable Expression<User> user) {
		if (user == null)
			return ((UserEvent) event).getUser();
		return user.getSingle(event);
	}
}
