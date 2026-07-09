package owlbe.skriptLuckPerms.modules.users.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

@SuppressWarnings("unchecked")
@Name("Revoke Permission")
@Description("Removes a permission from a user.")
@Example("""
function example(p: offlineplayer,perm: string):
    set {_lp} to luckperms user from {_p}
    edit user {_lp}:
        revoke permission {_perm}
    send "You just lost permission %{_perm}% ;c" to {_p}
        """)
@Since("1.0")
public class EffRevokePermission extends Effect {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffRevokePermission.class)
                        .addPatterns("(revoke|remove) [luckperm[s]] perm[ission] %string% [from %-luckpermsuser%]")
                        .build()
        );
    }

    private Expression<String> permissionExpr;
    private Expression<User> userExpr;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(SecEditUser.UserEvent.class)) {
            Skript.error("This can only be used inside an 'edit user' section");
            return false;
        }
        permissionExpr = (Expression<String>) expressions[0];
        userExpr = expressions[1] != null ? (Expression<User>) expressions[1] : null;
        return true;
    }

    @Override
    protected void execute(Event event) {
        String permission = permissionExpr.getSingle(event);
        User user = userExpr != null ? userExpr.getSingle(event) : ((SecEditUser.UserEvent) event).getUser();
        if (permission == null || user == null) return;
        user.data().remove(PermissionNode.builder(permission).build());
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append("revoke permission")
                .append(permissionExpr)
                .append("from")
                .append(userExpr)
                .toString();
    }

}
