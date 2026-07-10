package owlbe.skriptLuckPerms.modules.users.elements.sections;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.instance;

@Name("Edit User")
@Description("""
		Creates a section that allows you to modify the properties of the provided user.
		After the code in the section has finished the user will be saved asynchronously.
		""")
@Example("""
		edit user {_lp}:
			grant permission "mypermission"
	""")
@Since("1.0")
public class SecEditUser extends Section {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.SECTION,
				SyntaxInfo.builder(SecEditUser.class)
						.addPattern("edit [the] user %luckpermsuser%")
						.build()
		);

		eventValueRegistry.register(EventValue.builder(UserEvent.class, User.class)
				.getter(UserEvent::getUser)
				.patterns("user")
				.build());
	}
	private Expression<User> userExpr;

	@Nullable
	private Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult,
						@Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		userExpr = (Expression<User>) exprs[0];
		if (sectionNode != null) {
			trigger = SectionUtils.loadLinkedCode("user", (beforeLoading, afterLoading)
					-> loadCode(sectionNode, "user", beforeLoading, afterLoading, UserEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		if (trigger != null) {
			User user = userExpr.getSingle(event);
			if (user == null)
				return null;
			SecEditUser.UserEvent userevent = new SecEditUser.UserEvent(user);
			Object variables = Variables.copyLocalVariables(event);
			Variables.setLocalVariables(userevent,variables);
			TriggerItem.walk(trigger, userevent);
			Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
				LuckPermsProvider.get().getUserManager().saveUser(user);
				Bukkit.getScheduler().runTask(instance, () -> {
					Variables.setLocalVariables(event, Variables.copyLocalVariables(userevent));
					Variables.removeLocals(userevent);
					Variables.removeLocals(event);
				});

			});
		}
		return super.walk(event, false);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("edit user", userExpr)
				.toString();
	}

	public static class UserEvent extends Event {

		private final User user;

		public UserEvent(User user) {
			this.user = user;
		}

		public User getUser() {
			return user;
		}

		@Override
		@NotNull
		public HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}
