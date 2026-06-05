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
import net.luckperms.api.node.metadata.types.InheritanceOriginMetadata;
import net.luckperms.api.node.types.ChatMetaNode;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Chat Meta Source")
@Description("""
        Represents the source of a chatmeta.
        If inherited directly (not from a group) it will return the user's UUID.
        """)
@Example("""
        command /getsource:
            trigger:
                 get luckperms user player and store it in {_lp}
                 set {_g} to luckperms prefix of {_lp}
                 set {_source} to prefix source of {_g}
                 send "The source of your main prefix is %{_source}%!" to player
        """)
@Since("1.0")
public class ExprChatMetaSource extends SimpleExpression<String> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        syntaxRegistry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprChatMetaSource.class, String.class)
                        .addPatterns(
                                "[the] [luckperm[s]] (:prefix|suffix) source of %luckpermschatmeta%")
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
    protected String[] get(Event event) {
        ChatMetaNode node = chatMetaExpr.getSingle(event);
        if (node == null) return new String[0];
        InheritanceOriginMetadata origin = node.metadata(InheritanceOriginMetadata.KEY);
        String source = origin.getOrigin().getName();
        return new String[]{source};
    }
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append(isPrefix ? "prefix" : "suffix")
                .append("source of")
                .append(chatMetaExpr)
                .toString();
    }
}



