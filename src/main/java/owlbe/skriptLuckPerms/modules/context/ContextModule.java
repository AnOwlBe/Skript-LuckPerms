package owlbe.skriptLuckPerms.modules.context;

import ch.njol.skript.registrations.Classes;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import owlbe.skriptLuckPerms.modules.context.elements.StructContextCalculator;
import owlbe.skriptLuckPerms.modules.context.elements.expressions.ExprContext;
import owlbe.skriptLuckPerms.modules.context.elements.expressions.ExprContextKey;

public class ContextModule extends HierarchicalAddonModule {

	public ContextModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new ContextClassInfo());
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		register(addon,
				ExprContext::register,
				ExprContextKey::register,
				StructContextCalculator::register
		);
	}

	@Override
	public String name() {
		return "context";
	}

}
