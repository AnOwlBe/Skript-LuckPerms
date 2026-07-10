package owlbe.skriptLuckPerms.modules.groups;

import ch.njol.skript.registrations.Classes;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.groups.elements.effects.EffCreateGroup;
import owlbe.skriptLuckPerms.modules.groups.elements.effects.EffDeleteGroup;
import owlbe.skriptLuckPerms.modules.groups.elements.events.EvtGroupLosePermission;
import owlbe.skriptLuckPerms.modules.groups.elements.events.EvtGroupMetaRemove;
import owlbe.skriptLuckPerms.modules.groups.elements.events.EvtGroupMetaSet;
import owlbe.skriptLuckPerms.modules.groups.elements.events.EvtGroupReceivePermission;
import owlbe.skriptLuckPerms.modules.groups.elements.expressions.ExprAllGroups;
import owlbe.skriptLuckPerms.modules.groups.elements.sections.SecEditGroup;

import java.util.List;

public class GroupModule extends HierarchicalAddonModule {

	public GroupModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public Iterable<AddonModule> children() {
		return List.of();
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry registry = addon.registry(EventValueRegistry.class);
		register(addon,
				ExprAllGroups::register,
				EffCreateGroup::register,
				EffDeleteGroup::register,
				syntaxRegistry -> SecEditGroup.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtGroupLosePermission.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtGroupReceivePermission.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtGroupMetaSet.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtGroupMetaRemove.register(syntaxRegistry, registry)
		);

		Classes.registerClass(new GroupClassInfo());
		Converters.registerConverter(Group.class, String.class, Group::getName);
		Converters.registerConverter(String.class, Group.class, name -> LuckPermsProvider.get().getGroupManager().getGroup(name));
	}

	@Override
	public String name() {
		return "group";
	}

}
