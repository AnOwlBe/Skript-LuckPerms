package owlbe.skriptLuckPerms.modules.node.inheritance;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.util.Timespan;
import net.luckperms.api.node.types.InheritanceNode;

import javax.annotation.Nullable;
import java.time.Duration;

@SuppressWarnings({"UnstableApiUsage"})
public class InheritanceNodeClassInfo extends ClassInfo<InheritanceNode> {

	public InheritanceNodeClassInfo() {
		super(InheritanceNode.class, "luckpermsinheritance");
		this.user("luckperms ?inheritances?")
				.name("LuckPerms Inheritance Node")
				.description("Represents a LuckPerms inheritance node.")
				.since("INSERT VERSION")
				.parser(new InheritanceNodeParser());
	}

	private static class InheritanceNodeParser extends Parser<InheritanceNode> {
		//<editor-fold desc="inheritance node parser" defaultstate="collapsed">
		@Override
		public @Nullable InheritanceNode parse(String string, ParseContext context) {
			return null;
		}

		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(InheritanceNode node, int flags) {
			Duration duration = node.getExpiryDuration();
			String group = node.getGroupName();
			if (duration == null || duration.toMillis() == 0)
				return "inheritance node with group '" + group + "'";

			Timespan timespan = new Timespan(duration.toMillis());
			return "inheritance node with group '" +group + "' and expiry" + timespan;
		}

		@Override
		public String toVariableNameString(InheritanceNode node) {
			return node.getGroupName();
		}

		//</editor-fold>
	}

}
