package owlbe.skriptLuckPerms.modules.tracks.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.track.Track;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("All Groups In Track")
@Description("""
		Returns a list of groups in the specified track.
		""")
@Example("""
		command /getgroupsintrack:
			trigger:
				 send "Showing list of groups in track  'example'" to player
				 loop all of the groups in track "example":
					 send loop-value to player
		""")
@Since("1.0.2, INSERT VERSION (returns groups)")
public class ExprTrackGroups extends SimpleExpression<Group> {

	public static void register(SyntaxRegistry syntaxRegistry) {
		syntaxRegistry.register(
				SyntaxRegistry.EXPRESSION,
				SyntaxInfo.Expression.builder(ExprTrackGroups.class, Group.class)
						.addPattern("all [of the] [luckperm[s]] groups (of|in) track %luckpermstrack%")
						.build()
		);
	}

	private Expression<Track> track;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, ParseResult parseResult) {
		track = (Expression<Track>) expressions[0];
		return true;
	}

	@Override
	protected Group[] get(Event event) {
		Track track = this.track.getSingle(event);
		if (track == null)
			return new Group[0];

		return track.getGroups().stream()
				.map(group -> LuckPermsProvider.get().getGroupManager().getGroup(group))
				.toArray(Group[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Group> getReturnType() {
		return Group.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
				.append("all of the luckperms groups in track", track)
				.toString();
	}

}
