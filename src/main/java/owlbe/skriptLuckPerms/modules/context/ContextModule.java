package owlbe.skriptLuckPerms.modules.context;

import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import owlbe.skriptLuckPerms.modules.context.elements.StructContextCalculator;

public class ContextModule extends HierarchicalAddonModule {

	public ContextModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		register(addon,
				StructContextCalculator::register
		);
	}

	@Override
	public String name() {
		return "context";
	}

}
