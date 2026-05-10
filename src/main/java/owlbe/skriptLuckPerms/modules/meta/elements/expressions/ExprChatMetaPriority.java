package owlbe.skriptLuckPerms.modules.meta.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.luckperms.api.node.types.ChatMetaNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Chat Meta Priority")
@Description("""
        Represents the priority of a chat meta node.
        """)
@Example("""
        command /getpriority:
            trigger:
                 get luckperms user player and store it in {_lp}
                 set {_g} to highest luckperms group of {_lp}
                 set {_prefix} to group prefix of {_g}
                 set {_priority} to {_prefix}'s priority
                 send "Your group's priority is %{_priority}%!" to player
        """)
@Since("1.0")

public class ExprChatMetaPriority extends SimpleExpression<Integer> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        syntaxRegistry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprChatMetaPriority.class, Integer.class)
                        .addPatterns(
                                "[the] [luckperm[s]] (:prefix|suffix) priority of %luckpermschatmeta%")
                        .build()
        );
    }

    private Expression<ChatMetaNode> chatMetaExpr;
    private Boolean isPrefix;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        chatMetaExpr = (Expression<ChatMetaNode>) expressions[0];
        isPrefix = parseResult.hasTag("prefix");
        return true;
    }

    @Override
    protected Integer[] get(Event event) {
        ChatMetaNode node = chatMetaExpr.getSingle(event);
        if (node == null) return new Integer[0];
        int priority = node.getPriority();
        return new Integer[]{priority};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append(isPrefix ? "prefix" : "suffix")
                .append("priority of")
                .append(chatMetaExpr)
                .toString();
    }
}


