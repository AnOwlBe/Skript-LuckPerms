package owlbe.skriptLuckPerms.modules.groups.elements.sections;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SectionUtils;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
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

@Name("Edit Group")
@Description("""
		Creates a section that allows you to modify the properties of the provided group.
		After the code in the section has finished the group will be saved asynchronously.
		""")
@Example("""
	edit luckperms group "example":
		add 5 to weight of event-group
	""")
@Since("1.0, INSERT VERSION (edit luckpermsgroup)")
public class SecEditGroup extends Section {

	public static void register(SyntaxRegistry syntaxRegistry, EventValueRegistry registry) {
		syntaxRegistry.register(
				SyntaxRegistry.SECTION,
				SyntaxInfo.builder(SecEditGroup.class)
						.addPattern("edit [the] [luckperm[s]] group %luckpermsgroup%")
						.build()
		);

		registry.register(EventValue.builder(GroupEvent.class, Group.class)
				.getter(GroupEvent::getGroup)
				.patterns("group")
				.build());
	}
	private Expression<Group> group;

	@Nullable
	private Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult,
						@Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		group = (Expression<Group>) exprs[0];
		if (sectionNode != null) {
			trigger = SectionUtils.loadLinkedCode("group", (beforeLoading, afterLoading)
					-> loadCode(sectionNode, "group", beforeLoading, afterLoading, GroupEvent.class));
			return trigger != null;
		}
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		if (trigger != null) {
			Group group = this.group.getSingle(event);
			if (group == null)
				return null;
			SecEditGroup.GroupEvent groupevent = new SecEditGroup.GroupEvent(group);
			Object variables = Variables.copyLocalVariables(event);
			Variables.setLocalVariables(groupevent,variables);
			TriggerItem.walk(trigger, groupevent);
			Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
				LuckPermsProvider.get().getGroupManager().saveGroup(group);
				Bukkit.getScheduler().runTask(instance, () -> {
					Variables.setLocalVariables(event, Variables.copyLocalVariables(groupevent));
					Variables.removeLocals(groupevent);
					Variables.removeLocals(event);
				});

				});
		}
		return super.walk(event, false);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("edit group", group)
				.toString();
	}

	public static class GroupEvent extends Event {

		private final Group group;

		public GroupEvent(Group group) {
			this.group = group;
		}

		public Group getGroup() {
			return group;
		}

		@Override
		@NotNull
		public HandlerList getHandlers() {
			throw new IllegalStateException();
		}
	}

}
