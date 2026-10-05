package owlbe.skriptLuckPerms.modules.node.permission;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.luckperms.wrapper.PermissionNodeWrapper;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;

@SuppressWarnings({"UnstableApiUsage"})
public class PermissionWrapperClassInfo extends ClassInfo<PermissionNodeWrapper> {

	public PermissionWrapperClassInfo() {
		super(PermissionNodeWrapper.class, "luckpermspermissionwrapper");
		this.user("luckperms permission ?wrappers?")
				.name(NO_DOC)
				.description(NO_DOC)
				.since("INSERT VERSION")
				.defaultExpression(new EventValueExpression<>(PermissionNodeWrapper.class))
				.property(TYPED_VALUE,
						"The value of this permission.",
						SkriptLuckPerms.getAddonInstance(),
						new PermissionValueHandler());
	}

	private static class PermissionValueHandler implements TypedValueHandler<PermissionNodeWrapper, String> {
		//<editor-fold desc="permission node value handler" defaultstate="collapsed">


		@Override
		public @Nullable String convert(PermissionNodeWrapper node) {
			return node.key();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

}
