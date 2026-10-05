package owlbe.skriptLuckPerms.modules.permholder;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.Nullable;

public class PermHolderClassInfo extends ClassInfo<PermissionHolder> {

	public PermHolderClassInfo() {
		super(PermissionHolder.class, "luckpermspermissionholder");
		this.user("luckperms permission ?holders?")
				.name("LuckPerms Permission Holder")
				.description("Represents the parent to a LuckPerms user and group.")
				.since("INSERT VERSION")
				.parser(new PermissionHolderParser())
				.defaultExpression(new EventValueExpression<>(PermissionHolder.class));
	}

	private static class PermissionHolderParser extends Parser<PermissionHolder> {
		//<editor-fold desc="permission holder parser" defaultstate="collapsed">
		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public @Nullable PermissionHolder parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public String toString(PermissionHolder holder, int flags) {
			return switch (holder) {
				case User user -> "user '" + user.getFriendlyName() + "'";
				case Group group -> "group '" + group.getFriendlyName() + "'";
				default -> null;
			};
		}

		@Override
		public String toVariableNameString(PermissionHolder holder) {
			return holder.getFriendlyName();
		}

		//</editor-fold>
	}
}
