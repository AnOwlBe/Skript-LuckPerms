package owlbe.skriptLuckPerms.modules;

import ch.njol.skript.Skript;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import owlbe.skriptLuckPerms.modules.groups.GroupModule;
import owlbe.skriptLuckPerms.modules.meta.MetaModule;
import owlbe.skriptLuckPerms.modules.tracks.TrackModule;
import owlbe.skriptLuckPerms.modules.users.UserModule;

import java.util.List;

public class Modules extends HierarchicalAddonModule {

    @Override
    protected boolean canLoadSelf(SkriptAddon addon) {
        return Skript.classExists("org.bukkit.Bukkit");
    }

    @Override
    public Iterable<AddonModule> children() {
        return List.of(
                new GroupModule(this),
                new MetaModule(this),
                new TrackModule(this),
                new UserModule(this)
        );
    }

    @Override
    protected void initSelf(SkriptAddon addon) {

    }

    @Override
    protected void loadSelf(SkriptAddon addon) {
        // shrug
    }

    @Override
    public String name() {
        return "skript-luckperms";
    }

}
