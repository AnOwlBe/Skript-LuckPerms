package owlbe.skriptLuckPerms.modules.meta;

import ch.njol.skript.registrations.Classes;
import net.luckperms.api.node.types.ChatMetaNode;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.meta.elements.expressions.ExprPrefix;
import owlbe.skriptLuckPerms.modules.meta.elements.expressions.ExprSuffix;
import owlbe.skriptLuckPerms.modules.meta.elements.sections.SecChatMetaBuilder;

public class MetaModule extends HierarchicalAddonModule {

	public MetaModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		register(addon,
				ExprPrefix::register,
				ExprSuffix::register,
				SecChatMetaBuilder::register
		);

		Classes.registerClass(new MetaClassInfo());

		Converters.registerConverter(ChatMetaNode.class, String.class, ChatMetaNode::getMetaValue);
	}

	@Override
	public String name() {
		return "meta";
	}

}
