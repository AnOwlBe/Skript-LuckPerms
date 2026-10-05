package owlbe.skriptLuckPerms.modules.node.meta;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.properties.handlers.TypedValueHandler;
import owlbe.skriptLuckPerms.SkriptLuckPerms;
import owlbe.skriptLuckPerms.luckperms.wrapper.MetaNodeWrapper;

import static org.skriptlang.skript.lang.properties.Property.TYPED_VALUE;

@SuppressWarnings({"UnstableApiUsage"})
public class MetaWrapperClassInfo extends ClassInfo<MetaNodeWrapper> {

	public MetaWrapperClassInfo() {
		super(MetaNodeWrapper.class, "luckpermsmetawrapper");
		this.user("luckperms meta ?wrappers?")
				.name(NO_DOC)
				.description(NO_DOC)
				.since("INSERT VERSION")
				.defaultExpression(new EventValueExpression<>(MetaNodeWrapper.class))
				.property(TYPED_VALUE,
						"The value of this meta.",
						SkriptLuckPerms.getAddonInstance(),
						new MetaValueHandler());
	}


	private static class MetaValueHandler implements TypedValueHandler<MetaNodeWrapper, String> {
		//<editor-fold desc="meta node value handler" defaultstate="collapsed">

		@Override
		public @Nullable String convert(MetaNodeWrapper node) {
			return node.metaValue();
		}

		@Override
		public @NotNull Class<String> returnType() {
			return String.class;
		}
		//</editor-fold>
	}


}
