package owlbe.skriptLuckPerms.modules.meta;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.luckperms.api.node.types.ChatMetaNode;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.meta.elements.expressions.ExprChatMetaPriority;
import owlbe.skriptLuckPerms.modules.meta.elements.expressions.ExprChatMetaSource;
import owlbe.skriptLuckPerms.modules.meta.elements.expressions.ExprPrefix;
import owlbe.skriptLuckPerms.modules.meta.elements.expressions.ExprSuffix;
import owlbe.skriptLuckPerms.modules.meta.elements.sections.SecChatMetaBuilder;

import javax.annotation.Nullable;
import java.util.List;

public class MetaModule extends HierarchicalAddonModule {

    public MetaModule(AddonModule parentModule) {
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
                ExprPrefix::register,
                ExprSuffix::register,
                ExprChatMetaPriority::register,
                ExprChatMetaSource::register,
                SecChatMetaBuilder::register

        );
        Classes.registerClass(new ClassInfo<>(ChatMetaNode.class, "luckpermschatmeta")
                .user("luckperms ?chatmetas?")
                .name("LuckPerms ChatMeta")
                .description("A LuckPerms prefix or suffix node.")
                .parser(new Parser<>() {
                    @Override
                    @Nullable
                    public ChatMetaNode parse(String s, ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toString(ChatMetaNode node, int i) {
                        return node.getMetaValue();
                    }

                    @Override
                    public String toVariableNameString(ChatMetaNode node) {
                        return node.getMetaValue();
                    }
                })
                .since("1.0"));
        Converters.registerConverter(ChatMetaNode.class, String.class, ChatMetaNode::getMetaValue);
    }

    @Override
    public String name() {
        return "meta";
    }

}
