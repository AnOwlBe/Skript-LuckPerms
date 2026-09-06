package owlbe.skriptLuckPerms.modules.permholder.user;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.luckperms.api.model.user.User;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import owlbe.skriptLuckPerms.modules.permholder.user.elements.conditions.CondIsLoaded;
import owlbe.skriptLuckPerms.modules.permholder.user.elements.effects.*;
import owlbe.skriptLuckPerms.modules.permholder.user.elements.events.EvtUserDemote;
import owlbe.skriptLuckPerms.modules.permholder.user.elements.events.EvtUserPromote;
import owlbe.skriptLuckPerms.modules.permholder.user.elements.expressions.ExprAllUsers;
import owlbe.skriptLuckPerms.modules.permholder.user.elements.expressions.ExprPlayerFromUser;
import owlbe.skriptLuckPerms.modules.permholder.user.elements.expressions.ExprQuickUser;

import javax.annotation.Nullable;

public class UserModule extends HierarchicalAddonModule {

	public UserModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new ClassInfo<>(User.class, "luckpermsuser")
				.user("luckperms ?users?")
				.name("LuckPerms User")
				.description("Represents a LuckPerms user.")
				.parser(new Parser<>() {
					@Override
					public @Nullable User parse(String string, ParseContext context) {
						return null;
					}

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(User user, int flags) {
						return "user '" + user.getFriendlyName() + "'";
					}

					@Override
					public String toVariableNameString(User user) {
						return user.getUniqueId().toString();
					}
				})
				.since("1.0"));
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
				CondIsLoaded::register,
				ExprAllUsers::register,
				ExprPlayerFromUser::register,
				ExprQuickUser::register,
				EffDemoteUser::register,
				EffGroupMembers::register,
				EffLoadUser::register,
				EffPermissionMembers::register,
				EffPromoteUser::register,
				syntaxRegistry -> EvtUserPromote.register(syntaxRegistry, eventValueRegistry),
				syntaxRegistry -> EvtUserDemote.register(syntaxRegistry, eventValueRegistry)
				);
	}

	@Override
	public String name() {
		return "user";
	}

}
