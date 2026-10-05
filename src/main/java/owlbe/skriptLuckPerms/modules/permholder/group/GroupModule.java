package owlbe.skriptLuckPerms.modules.permholder.group;

import ch.njol.skript.registrations.Classes;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import owlbe.skriptLuckPerms.modules.permholder.group.elements.effects.EffCreateGroup;
import owlbe.skriptLuckPerms.modules.permholder.group.elements.effects.EffDeleteGroup;
import owlbe.skriptLuckPerms.modules.permholder.group.elements.expressions.ExprAllGroups;
import owlbe.skriptLuckPerms.modules.permholder.group.elements.expressions.ExprGroupFromName;
import owlbe.skriptLuckPerms.modules.permholder.group.elements.expressions.ExprGroups;

public class GroupModule extends HierarchicalAddonModule {

	public GroupModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new GroupClassInfo());
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
				ExprAllGroups::register,
				ExprGroupFromName::register,
				ExprGroups::register,
				EffCreateGroup::register,
				EffDeleteGroup::register
		);
	}

	@Override
	public String name() {
		return "group";
	}

}
