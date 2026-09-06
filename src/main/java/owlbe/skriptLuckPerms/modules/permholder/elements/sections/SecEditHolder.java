package owlbe.skriptLuckPerms.modules.permholder.elements.sections;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.group.Group;
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

@Name("Edit Permission Holder")
@Description("""
		Creates a section that allows you to modify the properties of the provided holder.
		After the code in the section has finished the holder will be saved asynchronously.
		""")
@Example("""
		edit user {_lp}:
			grant permission "mypermission"
	""")
@Since({"1.0", "INSERT VERSION ('luckperms' required"})
public class SecEditHolder extends Section {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.SECTION,
				SyntaxInfo.builder(SecEditHolder.class)
						.addPattern("edit [the] luckperm[s] (user|group|perm[ission] holder) %luckpermspermissionholder%")
						.build()
		);

		eventValueRegistry.register(EventValue.builder(HolderSectionEvent.class, User.class)
				.getter(event -> event.getHolder() instanceof User user ? user : null)
				.patterns("user")
				.build());

		eventValueRegistry.register(EventValue.builder(HolderSectionEvent.class, Group.class)
				.getter(event -> event.getHolder() instanceof Group group ? group : null)
				.patterns("group")
				.build());

		eventValueRegistry.register(EventValue.builder(HolderSectionEvent.class, PermissionHolder.class)
				.getter(HolderSectionEvent::getHolder)
				.patterns("[perm[ission]] holder")
				.build());
	}

	private Expression<PermissionHolder> holder;

	private @Nullable Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult,
						@Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		holder = (Expression<PermissionHolder>) expressions[0];

		if (sectionNode != null) {
			trigger = SectionUtils.loadLinkedCode("permission holder", (beforeLoading, afterLoading)
					-> loadCode(sectionNode, "permission holder", beforeLoading, afterLoading, HolderSectionEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		if (trigger != null) {
			PermissionHolder holder = this.holder.getSingle(event);
			if (holder == null)
				return null;

			HolderSectionEvent holderEvent = new HolderSectionEvent(holder);

			Object variables = Variables.copyLocalVariables(event);
			Variables.setLocalVariables(holderEvent, variables);
			TriggerItem.walk(trigger, holderEvent);

			Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
				switch (holder) {
					case Group group -> LuckPermsProvider.get().getGroupManager().saveGroup(group);
					case User user -> LuckPermsProvider.get().getUserManager().saveUser(user);
					default -> throw new IllegalStateException("Unexpected value: " + holder);
				}

				Bukkit.getScheduler().runTask(instance, () -> {
					Variables.setLocalVariables(event, Variables.copyLocalVariables(holderEvent));
					Variables.removeLocals(holderEvent);
					Variables.removeLocals(event);
				});

			});
		}
		return super.walk(event, false);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "edit luckperms permission holder " + holder.toString(event, debug);
	}

	public static class HolderSectionEvent extends Event {

		private final PermissionHolder holder;

		public HolderSectionEvent(PermissionHolder holder) {
			this.holder = holder;
		}

		public PermissionHolder getHolder() {
			return holder;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}

