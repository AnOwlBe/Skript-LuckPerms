package owlbe.skriptLuckPerms.modules.permission;

import ch.njol.skript.registrations.Classes;
import net.luckperms.api.node.types.PermissionNode;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.permission.elements.expressions.SecExprPermissionBuilder;

public class PermissionModule extends HierarchicalAddonModule {

	public PermissionModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		register(addon,
				SecExprPermissionBuilder::register
		);

		Classes.registerClass(new PermissionClassInfo());
		Converters.registerConverter(PermissionNode.class, String.class, PermissionNode::getPermission);
		Converters.registerConverter(String.class, PermissionNode.class, permission -> PermissionNode.builder(permission).build());
	}

	@Override
	public String name() {
		return "permission";
	}

}
