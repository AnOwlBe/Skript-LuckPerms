package owlbe.skriptLuckPerms.modules.node.permission;

import ch.njol.skript.registrations.Classes;
import net.luckperms.api.node.types.PermissionNode;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.node.permission.elements.expressions.ExprSecCreatePermission;

public class PermissionModule extends HierarchicalAddonModule {

	public PermissionModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new PermissionClassInfo());
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
				syntaxRegistry -> ExprSecCreatePermission.register(syntaxRegistry, eventValueRegistry)
		);

		Converters.registerConverter(PermissionNode.class, String.class, PermissionNode::getPermission);
		Converters.registerConverter(String.class, PermissionNode.class, permission -> PermissionNode.builder(permission).build());
	}

	@Override
	public String name() {
		return "permission node";
	}

}
