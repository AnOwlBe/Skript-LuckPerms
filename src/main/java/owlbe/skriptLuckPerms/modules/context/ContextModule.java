package owlbe.skriptLuckPerms.modules.context;

import net.luckperms.api.context.*;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;

public class ContextModule extends HierarchicalAddonModule {

	public ContextModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		ImmutableContextSet test = ImmutableContextSet.builder()
				.add("server", "survival")
				.build();

		register(addon);
	}

	@Override
	public String name() {
		return "meta";
	}

}
