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
import owlbe.skriptLuckPerms.SkriptLuckPerms;

import java.util.Arrays;
import java.util.List;

@Name("Edit Permission Holder")
@Description("""
		Creates a section that allows you to modify the properties of the provided holder(s).
		After the code in the section has finished the holder(s) will be saved asynchronously.
		""")
@Example("""
		edit user {_lp}:
			grant permission "mypermission"
	""")
@Since("INSERT VERSION")
public class SecEditHolder extends Section {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry eventValueRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.SECTION,
				SyntaxInfo.builder(SecEditHolder.class)
						.addPattern("edit [the] luckperm[s] (user|group|perm[ission] holder)[s] %luckpermspermissionholders%")
						.build()
		);

		eventValueRegistry.register(EventValue.builder(HolderSectionEvent.class, User[].class)
				.getter(event -> Arrays.stream(event.getHolders())
						.map(holder -> holder instanceof User user ? user : null)
						.toArray(User[]::new))
				.patterns("user[s]")
				.build());

		eventValueRegistry.register(EventValue.builder(HolderSectionEvent.class, Group[].class)
				.getter(event -> Arrays.stream(event.getHolders())
						.map(holder -> holder instanceof Group group ? group : null)
						.toArray(Group[]::new))
				.patterns("group[s]")
				.build());

		eventValueRegistry.register(EventValue.builder(HolderSectionEvent.class, PermissionHolder[].class)
				.getter(event -> Arrays.stream(event.getHolders()).toArray(PermissionHolder[]::new))
				.patterns("[perm[ission]] holder[s]")
				.build());


	}

	private Expression<PermissionHolder> holders;

	private @Nullable Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(
			Expression<?>[] expressions,
			int matchedPattern,
			Kleenean isDelayed,
			ParseResult parseResult,
			@Nullable SectionNode sectionNode,
			@Nullable List<TriggerItem> triggerItems) {

		getParser().setHasDelayBefore(Kleenean.TRUE);
		holders = (Expression<PermissionHolder>) expressions[0];

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
			PermissionHolder[] holders = this.holders.getArray(event);
			if (holders == null)
				return null;

			HolderSectionEvent holderEvent = new HolderSectionEvent(holders);

			Object variables = Variables.copyLocalVariables(event);
			Variables.setLocalVariables(holderEvent, variables);
			TriggerItem.walk(trigger, holderEvent);

			Bukkit.getScheduler().runTaskAsynchronously(SkriptLuckPerms.getPluginInstance(), () -> {
				for (PermissionHolder holder : holders) {
					switch (holder) {
						case Group group -> LuckPermsProvider.get().getGroupManager().saveGroup(group);
						case User user -> LuckPermsProvider.get().getUserManager().saveUser(user);
						default -> throw new IllegalStateException("Unexpected value: " + holder);
					}
				}

				Bukkit.getScheduler().runTask(SkriptLuckPerms.getPluginInstance(), () -> {
					Variables.setLocalVariables(event, Variables.copyLocalVariables(holderEvent));
					Variables.removeLocals(holderEvent);
					Variables.removeLocals(event);
					TriggerItem.walk(getNext(), holderEvent);
				});

			});
		}
		return super.walk(event, false);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "edit luckperms permission holders " + holders.toString(event, debug);
	}

	public static class HolderSectionEvent extends Event {

		private final PermissionHolder[] holders;

		public HolderSectionEvent(PermissionHolder... holders) {
			this.holders = holders;
		}

		public PermissionHolder[] getHolders() {
			return holders;
		}

		@Override
		public @NotNull HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}

