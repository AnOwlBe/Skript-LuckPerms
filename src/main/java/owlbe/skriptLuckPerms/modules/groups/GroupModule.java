package owlbe.skriptLuckPerms.modules.groups;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.groups.elements.effects.EffCreateGroup;
import owlbe.skriptLuckPerms.modules.groups.elements.effects.EffDeleteGroup;
import owlbe.skriptLuckPerms.modules.groups.elements.events.EvtGroupLosePermission;
import owlbe.skriptLuckPerms.modules.groups.elements.events.EvtGroupMetaRemove;
import owlbe.skriptLuckPerms.modules.groups.elements.events.EvtGroupMetaSet;
import owlbe.skriptLuckPerms.modules.groups.elements.events.EvtGroupReceivePermission;
import owlbe.skriptLuckPerms.modules.groups.elements.expressions.ExprAllGroups;
import owlbe.skriptLuckPerms.modules.groups.elements.expressions.ExprGroupWeight;
import owlbe.skriptLuckPerms.modules.groups.elements.sections.SecEditGroup;

import javax.annotation.Nullable;
import java.util.List;

public class GroupModule extends HierarchicalAddonModule {

    public GroupModule(AddonModule parentModule) {
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
                ExprAllGroups::register,
                ExprGroupWeight::register,
                EffCreateGroup::register,
                EffDeleteGroup::register,
                a -> SecEditGroup.register(a, registry),
                a -> EvtGroupLosePermission.register(a, registry),
                a -> EvtGroupReceivePermission.register(a, registry),
                a -> EvtGroupMetaSet.register(a, registry),
                a -> EvtGroupMetaRemove.register(a, registry)


        );
        Classes.registerClass(new ClassInfo<>(Group.class, "luckpermsgroup")
                .user("luckperms ?groups?")
                .name("LuckPerms Group")
                .description("A LuckPerms group.")
                .parser(new Parser<>() {
                    @Override
                    @Nullable
                    public Group parse(String s, ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(Group group, int flags) {
                        return group.getName();
                    }

                    @Override
                    public String toVariableNameString(Group group) {
                        return group.getName();
                    }
                })
                .since("1.0"));
        Converters.registerConverter(Group.class, String.class, Group::getName);
        Converters.registerConverter(String.class, Group.class, name -> LuckPermsProvider.get().getGroupManager().getGroup(name));
    }

    @Override
    public String name() {
        return "group";
    }

}
