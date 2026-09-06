package owlbe.skriptLuckPerms.modules.node;

import ch.njol.skript.registrations.Classes;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import owlbe.skriptLuckPerms.modules.node.chatmeta.ChatMetaModule;
import owlbe.skriptLuckPerms.modules.node.inheritance.InheritanceModule;
import owlbe.skriptLuckPerms.modules.node.meta.MetaModule;
import owlbe.skriptLuckPerms.modules.node.permission.PermissionModule;

import java.util.List;

public class NodeModule extends HierarchicalAddonModule {

	public NodeModule(AddonModule parentModule) {
		super(parentModule);
	}

	public Iterable<AddonModule> children() {
		return List.of(
				new ChatMetaModule(this),
				new InheritanceModule(this),
				new MetaModule(this),
				new PermissionModule(this)
		);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new NodeClassInfo());
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
	}

	@Override
	public String name() {
		return "node";
	}

}
