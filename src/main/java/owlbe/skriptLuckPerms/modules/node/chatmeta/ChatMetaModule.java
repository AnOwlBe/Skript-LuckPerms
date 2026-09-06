package owlbe.skriptLuckPerms.modules.node.chatmeta;

import ch.njol.skript.registrations.Classes;
import net.luckperms.api.node.types.ChatMetaNode;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.node.chatmeta.elements.expressions.ExprSecCreateChatMeta;

public class ChatMetaModule extends HierarchicalAddonModule {

	public ChatMetaModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new ChatMetaClassInfo());
		Classes.registerClass(new ChatMetaWrapperClassInfo());
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
				syntaxRegistry -> ExprSecCreateChatMeta.register(syntaxRegistry, eventValueRegistry)
		);

		Converters.registerConverter(ChatMetaNode.class, String.class, ChatMetaNode::getMetaValue);
	}

	@Override
	public String name() {
		return "chat meta";
	}

}
