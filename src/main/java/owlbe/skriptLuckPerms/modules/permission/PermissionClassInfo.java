package owlbe.skriptLuckPerms.modules.permission;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.Timespan;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.node.types.PermissionNode;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import javax.annotation.Nullable;
import java.time.Duration;

import static owlbe.skriptLuckPerms.SkriptLuckPerms.addon;
import static owlbe.skriptLuckPerms.skript.properties.Properties.*;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class PermissionClassInfo extends ClassInfo<PermissionNode> {

	public PermissionClassInfo() {
		super(PermissionNode.class, "luckpermspermission");
		this.user("luckperms ?permissions?")
				.name("Permission")
				.description("Represents a LuckPerms permission.")
				.since("INSERT VERSION")
				.parser(new PermissionParser())
				.property(getProperty(EXPIRY),
				"Expiry of this permission.",
				addon,
				new PermissionExpiryHandler());
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

	private static class PermissionExpiryHandler implements ExpressionPropertyHandler<PermissionNode, Timespan> {
		//<editor-fold desc="permission expiry handler" defaultstate="collapsed">

		@Override
		public @Nullable Timespan convert(PermissionNode permission) {
			Duration expiryDuration = permission.getExpiryDuration();
			if (expiryDuration == null)
				return new Timespan(0);
			return new Timespan(expiryDuration.toMillis());
		}

		@Override
		public @NotNull Class<Timespan> returnType() {
			return Timespan.class;
		}
		//</editor-fold>
	}

}
