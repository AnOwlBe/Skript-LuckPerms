package owlbe.skriptLuckPerms.modules.users;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.luckperms.api.model.user.User;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import owlbe.skriptLuckPerms.modules.users.elements.conditions.CondHasGroup;
import owlbe.skriptLuckPerms.modules.users.elements.conditions.CondHasPermission;
import owlbe.skriptLuckPerms.modules.users.elements.effects.*;
import owlbe.skriptLuckPerms.modules.users.elements.events.*;
import owlbe.skriptLuckPerms.modules.users.elements.expressions.ExprGroupsOfUser;
import owlbe.skriptLuckPerms.modules.users.elements.expressions.ExprPermissionsOfUser;
import owlbe.skriptLuckPerms.modules.users.elements.expressions.ExprPlayerFromUser;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

import javax.annotation.Nullable;

public class UserModule extends HierarchicalAddonModule {

	public UserModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry registry = addon.registry(EventValueRegistry.class);
		register(addon,
				//ExprGroupsOfUser::register,
				//ExprPermissionsOfUser::register,
				ExprPlayerFromUser::register,
				CondHasGroup::register,
				CondHasPermission::register,
				EffGrantGroup::register,
				EffRevokePermission::register,
				EffDemoteUser::register,
				EffGrantPermission::register,
				EffGroupMembers::register,
				EffLoadPlayer::register,
				EffQuickLoadPlayer::register,
				EffPermissionMembers::register,
				EffPromoteUser::register,
				EffRevokeGroup::register,
				syntaxRegistry -> SecEditUser.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtUserReceivePermission.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtUserReceiveGroup.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtUserPromote.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtUserDemote.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtUserMetaSet.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtUserMetaRemove.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtUserLosePermission.register(syntaxRegistry, registry),
				syntaxRegistry -> EvtUserLoseGroup.register(syntaxRegistry, registry)
				);

		Classes.registerClass(new ClassInfo<>(User.class, "luckpermsuser")
				.user("luckperms ?users?")
				.name("LuckPerms User")
				.description("A LuckPerms user.")
				.parser(new Parser<>() {
					@Override
					@Nullable
					public User parse(String string, ParseContext context) {
						return null;
					}

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(User user, int flags) {
						return user.getFriendlyName();
					}

					@Override
					public String toVariableNameString(User user) {
						return user.getUniqueId().toString();
					}
				})
				.since("1.0"));
	}

	@Override
	public String name() {
		return "user";
	}

}
