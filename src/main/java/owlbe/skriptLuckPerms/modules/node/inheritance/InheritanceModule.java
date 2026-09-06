package owlbe.skriptLuckPerms.modules.node.inheritance;

import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import owlbe.skriptLuckPerms.modules.node.inheritance.elements.expressions.ExprSecCreateInheritance;

public class InheritanceModule extends HierarchicalAddonModule {

	public InheritanceModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
				syntaxRegistry -> ExprSecCreateInheritance.register(syntaxRegistry, eventValueRegistry)
		);
	}

	@Override
	public String name() {
		return "inheritance node";
	}

}
