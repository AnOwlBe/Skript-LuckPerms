package owlbe.skriptLuckPerms.modules.track;

import ch.njol.skript.registrations.Classes;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.track.Track;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;
import org.skriptlang.skript.lang.converter.Converters;
import owlbe.skriptLuckPerms.modules.track.elements.effects.EffCreateTrack;
import owlbe.skriptLuckPerms.modules.track.elements.effects.EffDeleteTrack;
import owlbe.skriptLuckPerms.modules.track.elements.effects.EffLoadTrack;
import owlbe.skriptLuckPerms.modules.track.elements.events.EvtTrackAddGroup;
import owlbe.skriptLuckPerms.modules.track.elements.events.EvtTrackRemoveGroup;
import owlbe.skriptLuckPerms.modules.track.elements.expressions.ExprAllTracks;
import owlbe.skriptLuckPerms.modules.track.elements.sections.SecEditTrack;

public class TrackModule extends HierarchicalAddonModule {

	public TrackModule(AddonModule parentModule) {
		super(parentModule);
	}


	@Override
	public void initSelf(SkriptAddon addon) {
		Classes.registerClass(new TrackClassInfo());
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		EventValueRegistry eventValueRegistry = addon.registry(EventValueRegistry.class);

		register(addon,
				EffCreateTrack::register,
				EffDeleteTrack::register,
				EffLoadTrack::register,
				syntaxRegistry -> EvtTrackAddGroup.register(syntaxRegistry, eventValueRegistry),
				syntaxRegistry -> EvtTrackRemoveGroup.register(syntaxRegistry, eventValueRegistry),
				ExprAllTracks::register,
				syntaxRegistry -> SecEditTrack.register(syntaxRegistry, eventValueRegistry)
				);

		Converters.registerConverter(Track.class, String.class, Track::getName);
		Converters.registerConverter(String.class, Track.class, name -> LuckPermsProvider.get().getTrackManager().getTrack(name));
	}

	@Override
	public String name() {
		return "track";
	}

}
