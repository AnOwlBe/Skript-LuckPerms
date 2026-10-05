package owlbe.skriptLuckPerms.modules.node.meta;

import ch.njol.skript.registrations.Classes;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import owlbe.skriptLuckPerms.modules.node.meta.elements.expressions.ExprMetaKey;
import owlbe.skriptLuckPerms.modules.node.meta.elements.expressions.ExprSecCreateMeta;

public class MetaModule extends HierarchicalAddonModule {

	public MetaModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new MetaNodeClassInfo());
		Classes.registerClass(new MetaWrapperClassInfo());
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
				ExprMetaKey::register,
				syntaxRegistry -> ExprSecCreateMeta.register(syntaxRegistry, eventValueRegistry)
		);
	}

	@Override
	public String name() {
		return "meta node";
	}

}
