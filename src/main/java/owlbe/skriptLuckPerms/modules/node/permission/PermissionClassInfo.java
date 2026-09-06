package owlbe.skriptLuckPerms.modules.node.permission;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.Timespan;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.node.types.PermissionNode;

import javax.annotation.Nullable;
import java.time.Duration;

public class PermissionClassInfo extends ClassInfo<PermissionNode> {

	public PermissionClassInfo() {
		super(PermissionNode.class, "luckpermspermission");
		this.user("luckperms ?permissions?")
				.name("LuckPerms Permission")
				.description("Represents a LuckPerms permission.")
				.since("INSERT VERSION")
				.parser(new PermissionParser());
	}

	private static class PermissionParser extends Parser<PermissionNode> {
		//<editor-fold desc="permission parser" defaultstate="collapsed">
		@Override
		@Nullable
		public PermissionNode parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(PermissionNode permission, int flags) {
			Duration duration = permission.getExpiryDuration();
			String key = permission.getKey();
			ImmutableContextSet context = permission.getContexts();

			if (duration == null || duration.toMillis() == 0)
				return "permission '" + key + "' with context " + context;

			Timespan timespan = new Timespan(duration.toMillis());
			return "permission '" + key + "' with duration " + timespan + " with context " + context;
		}

		@Override
		public String toVariableNameString(PermissionNode permission) {
			return permission.getKey();
		}

		//</editor-fold>
	}

}
