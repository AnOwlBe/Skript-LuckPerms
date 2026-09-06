package owlbe.skriptLuckPerms.modules.test;

import ch.njol.skript.test.runner.TestMode;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.Track;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.HierarchicalAddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import owlbe.skriptLuckPerms.modules.test.elements.expressions.ExprTestGroup;
import owlbe.skriptLuckPerms.modules.test.elements.expressions.ExprTestTrack;
import owlbe.skriptLuckPerms.modules.test.elements.expressions.ExprTestUser;

import java.util.UUID;


public class TestModule extends HierarchicalAddonModule {

	private static Group group;
	private static Track track;
	private static User user;

	public TestModule(AddonModule parentModule) {
		super(parentModule);
	}

	@Override
	protected boolean canLoadSelf(SkriptAddon addon) {
		return TestMode.ENABLED;
	}

	@Override
	public void loadSelf(SkriptAddon addon) {
		group = LuckPermsProvider.get().getGroupManager()
				.createAndLoadGroup("test")
				.join();

		track = LuckPermsProvider.get().getTrackManager()
				.createAndLoadTrack("test")
				.join();

		user = LuckPermsProvider.get().getUserManager()
				.loadUser(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"))
				.join();

		register(addon,
				ExprTestGroup::register,
				ExprTestTrack::register,
				ExprTestUser::register
				);
	}

	@Override
	public String name() {
		return "test";
	}

	/**
	 * A LuckPerms group to be used during tests.
	 * @return The LuckPerms group
	 */
	public static @NotNull Group getTestGroup() {
		return group;
	}

	/**
	 * A LuckPerms track to be used during tests.
	 * @return The LuckPerms track
	 */
	public static @NotNull Track getTestTrack() {
		return track;
	}

	/**
	 * A LuckPerms user to be used during tests.
	 * @return The LuckPerms user.
	 */
	public static @NotNull User getTestUser() {
		return user;
	}

}
