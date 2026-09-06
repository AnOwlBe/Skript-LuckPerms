package owlbe.skriptLuckPerms.modules.permholder;

import ch.njol.skript.registrations.Classes;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import owlbe.skriptLuckPerms.luckperms.bukkitevents.parent.PermissionHolderEvent;
import owlbe.skriptLuckPerms.modules.permholder.elements.conditions.CondHasGroup;
import owlbe.skriptLuckPerms.modules.permholder.elements.conditions.CondHasPermission;
import owlbe.skriptLuckPerms.modules.permholder.elements.events.*;
import owlbe.skriptLuckPerms.modules.permholder.elements.expressions.*;
import owlbe.skriptLuckPerms.modules.permholder.elements.sections.SecEditHolder;
import owlbe.skriptLuckPerms.modules.permholder.group.GroupModule;
import owlbe.skriptLuckPerms.modules.permholder.user.UserModule;

import java.util.List;

public class PermHolderModule extends HierarchicalAddonModule {

	public PermHolderModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public Iterable<AddonModule> children() {
		return List.of(
				new GroupModule(this),
				new UserModule(this)
		);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new PermHolderClassInfo());
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		registerEventValues(eventValueRegistry);

		register(addon,
				syntaxRegistry -> EvtGroupAdd.register(syntaxRegistry, eventValueRegistry),
				syntaxRegistry -> EvtGroupRemove.register(syntaxRegistry, eventValueRegistry),
				syntaxRegistry -> EvtMetaAdd.register(syntaxRegistry, eventValueRegistry),
				syntaxRegistry -> EvtMetaRemove.register(syntaxRegistry, eventValueRegistry),
				syntaxRegistry -> EvtPermissionAdd.register(syntaxRegistry, eventValueRegistry),
				syntaxRegistry -> EvtPermissionRemove.register(syntaxRegistry, eventValueRegistry),
				CondHasGroup::register,
				CondHasPermission::register,
				ExprInheritances::register,
				ExprMeta::register,
				ExprPermissions::register,
				ExprPrefix::register,
				ExprSuffix::register,
				syntaxRegistry -> SecEditHolder.register(syntaxRegistry, eventValueRegistry)
		);
	}

	private static void registerEventValues(EventValueRegistry eventValueRegistry) {
		eventValueRegistry.register(EventValue.builder(PermissionHolderEvent.class, Group.class)
				.getter(event -> event.getTarget() instanceof Group group ? group : null)
				.patterns("group")
				.build());

		eventValueRegistry.register(EventValue.builder(PermissionHolderEvent.class, OfflinePlayer.class)
				.getter(event -> {
					if (event.getTarget() instanceof User user)
						return Bukkit.getOfflinePlayer(user.getUniqueId());
					return null;
				})
				.build());

		eventValueRegistry.register(EventValue.builder(PermissionHolderEvent.class, Node.class)
				.getter(PermissionHolderEvent::getNode)
				.patterns("node")
				.build());
	}

	@Override
	public String name() {
		return "permission holder";
	}

}