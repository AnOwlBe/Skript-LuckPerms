package owlbe.skriptLuckPerms.modules;

import ch.njol.skript.Skript;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import owlbe.skriptLuckPerms.modules.context.ContextModule;
import owlbe.skriptLuckPerms.modules.node.NodeModule;
import owlbe.skriptLuckPerms.modules.permholder.PermHolderModule;
import owlbe.skriptLuckPerms.modules.test.TestModule;
import owlbe.skriptLuckPerms.modules.track.TrackModule;

import java.util.List;

public class Modules extends HierarchicalAddonModule {

    @Override
    protected boolean canLoadSelf(SkriptAddon addon) {
        return Skript.classExists("org.bukkit.Bukkit");
    }

    @Override
    public Iterable<AddonModule> children() {
        return List.of(
                new ContextModule(this),
                new NodeModule(this),
                new PermHolderModule(this),
                new TestModule(this),
                new TrackModule(this)
        );
    }

    @Override
    protected void loadSelf(SkriptAddon addon) {
        // nothing to do
    }

    @Override
    public String name() {
        return "skript-luckperms";
    }

}
