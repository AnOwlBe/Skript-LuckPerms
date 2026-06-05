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
import owlbe.skriptLuckPerms.modules.users.elements.expressions.ExprGroupsOf;
import owlbe.skriptLuckPerms.modules.users.elements.expressions.ExprPermissionsOf;
import owlbe.skriptLuckPerms.modules.users.elements.expressions.ExprPlayerFromUser;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

import javax.annotation.Nullable;
import java.util.List;

public class UserModule extends HierarchicalAddonModule {

    public UserModule(AddonModule parentModule) {
        super(parentModule);
    }

    @Override
    public Iterable<AddonModule> children() {
        return List.of();
    }

    @Override
    public void loadSelf(SkriptAddon addon) {
        EventValueRegistry registry = addon.registry(EventValueRegistry.class);
        register(addon,
                ExprGroupsOf::register,
                ExprPermissionsOf::register,
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
                a -> SecEditUser.register(a, registry),
                a -> EvtUserReceivePermission.register(a, registry),
                a -> EvtUserReceiveGroup.register(a, registry),
                a -> EvtUserPromote.register(a, registry),
                a -> EvtUserDemote.register(a, registry),
                a -> EvtUserMetaSet.register(a, registry),
                a -> EvtUserMetaRemove.register(a, registry),
                a -> EvtUserLosePermission.register(a, registry),
                a -> EvtUserLoseGroup.register(a, registry)



                );
        Classes.registerClass(new ClassInfo<>(User.class, "luckpermsuser")
                .user("luckperms ?users?")
                .name("LuckPerms User")
                .description("A LuckPerms user.")
                .parser(new Parser<>() {
                    @Override
                    @Nullable
                    public User parse(String s, ParseContext context) {
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
