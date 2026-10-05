package owlbe.skriptLuckPerms.modules.node.inheritance;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.luckperms.wrapper.InheritanceNodeWrapper;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;

@SuppressWarnings({"UnstableApiUsage"})
public class InheritanceWrapperClassInfo extends ClassInfo<InheritanceNodeWrapper> {

	public InheritanceWrapperClassInfo() {
		super(InheritanceNodeWrapper.class, "luckpermsinheritancewrapper");
		this.user("luckperms inheritance ?wrappers?")
				.name(NO_DOC)
				.description(NO_DOC)
				.since("INSERT VERSION")
				.defaultExpression(new EventValueExpression<>(InheritanceNodeWrapper.class))
				.property(TYPED_VALUE,
						"The value of this inheritance.",
						SkriptLuckPerms.getAddonInstance(),
						new InheritanceValueHandler());
	}

	private static class InheritanceValueHandler implements TypedValueHandler<InheritanceNodeWrapper, String> {
		//<editor-fold desc="inheritance node value handler" defaultstate="collapsed">


		@Override
		public @Nullable String convert(InheritanceNodeWrapper node) {
			return node.group().getName();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}

}
