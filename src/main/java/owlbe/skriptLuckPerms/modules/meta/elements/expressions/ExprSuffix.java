package owlbe.skriptLuckPerms.modules.meta.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import net.luckperms.api.node.types.SuffixNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import owlbe.skriptLuckPerms.modules.groups.elements.sections.SecEditGroup;
import owlbe.skriptLuckPerms.modules.users.elements.sections.SecEditUser;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Name("Suffix")
@Description("""
        Returns the primary suffix of a user/group.
        If `suffixes` is used it will return a sorted list of all suffixes of the user/group.
        Use `ExprChatMetaPriority` to get priority of a suffix.
        """)
@Example("""
        function get(p: offlineplayer):
            get luckperms user {_p} and store it in {_lp}
            set {_suffix} to formatted luckperms suffix of {_lp}
            set {_suffixes::*} to luckperms suffixes of {_lp}
            if {_p} is online:
                 send "Your suffix: %{_suffix}%" to {_p}
                 send "You have %size of {_suffixes::*}% suffixes!" to {_p}
                 loop {_suffixes::*}:
                      set {_suffix} to formatted loop-value
                      send "Priority: %suffix priority of loop-value% Suffix: %{_suffix}%" to {_p}
        """)
@Example("""
        function get(group: string):
            set {_prefix} to formatted luckperms prefix of {_group}
            set {_prefixes::*} to luckperms prefixes of {_group}
            broadcast "%{_group}%'s primary prefix: %{_prefix}%"
            broadcast "Amount of all prefixes: %size of {_prefixes::*}%x"
            broadcast "All:"
                 loop {_prefixes::*}:
                      set {_prefix} to formatted loop-value
                      broadcast "Priority: %prefix priority of loop-value% Prefix: %{_prefix}%"
        """)
@Since("1.0")
public class ExprSuffix extends SimpleExpression<ChatMetaNode> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        syntaxRegistry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprSuffix.class, ChatMetaNode.class)
                        .addPatterns(
                                "[the] [luckperm[s]] suffix[:es] of group %luckpermsgroup%",
                                "[the] [luckperm[s]] suffix[:es] of user %luckpermsuser%")
                        .build()
        );
    }

    private Expression<Group> groupExpr;
    private Expression<User> userExpr;
    private boolean isSingle;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (i == 0) {
            groupExpr = (Expression<Group>) expressions[0];
        } else {
            userExpr = (Expression<User>) expressions[0];
        }
        isSingle = !parseResult.hasTag("es");
        return true;
    }
    @Override
    protected ChatMetaNode<?,?>[] get(Event event) {
        Collection<SuffixNode> nodes = null;
        if (groupExpr != null) {
            Group group = groupExpr.getSingle(event);
            if (group == null) return new ChatMetaNode[0];
            nodes = group.getNodes(NodeType.SUFFIX);
        } else if (userExpr != null) {
            User user = userExpr.getSingle(event);
            if (user == null) return new ChatMetaNode[0];
            // sadly user.getNodes(NodeType.SUFFIX) doesn't include inherited prefixes ;c
            nodes = user.resolveInheritedNodes(QueryOptions.nonContextual())
                    .stream()
                    .filter(NodeType.SUFFIX::matches)
                    .map(NodeType.SUFFIX::cast)
                    .collect(Collectors.toList());
        }
        if (nodes == null) {
            return new ChatMetaNode[0];
        }
        Stream<SuffixNode> stream = nodes.stream()
                .sorted(Comparator.comparingInt((SuffixNode node) -> node.getPriority()).reversed());
        if (isSingle) {
            return stream.findFirst().map(node -> new ChatMetaNode[]{node}).orElse(new ChatMetaNode[0]);
        } else {
            return stream.toArray(ChatMetaNode[]::new);
        }
    }
    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        if (groupExpr != null && !getParser().isCurrentEvent(SecEditGroup.GroupEvent.class)) {
            Skript.error("This can only be used inside an 'edit group' section");
            return null;
        }
        if (userExpr != null && !getParser().isCurrentEvent(SecEditUser.UserEvent.class)) {
            Skript.error("This can only be used inside an 'edit user' section");
            return null;
        }
        return switch (mode) {
            case SET, ADD, RESET, REMOVE ->
                    CollectionUtils.array(ChatMetaNode[].class,ChatMetaNode.class); // ChatMetaNode.class is needed for SET (idk maybe better way)
            default -> null;
        };
    }
    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        Group group = groupExpr != null ? groupExpr.getSingle(event) : null;
        if (group == null && event instanceof SecEditGroup.GroupEvent e) {
            group = LuckPermsProvider.get().getGroupManager().getGroup(e.getGroup());
        }
        User user = userExpr != null ? userExpr.getSingle(event) : null;
        if (user == null && event instanceof SecEditUser.UserEvent e) user = e.getUser();
        NodeMap data = group != null ? group.data() : user != null ? user.data() : null;
        if (data == null) return;
        switch (mode) {
            case ADD -> {
                if (isSingle) {
                    Skript.warning("Cannot add to a primary suffix — use 'suffixes' instead.");
                    return;
                }
                if (delta == null) return;
                ChatMetaNode node = (ChatMetaNode) delta[0];
                data.add(node);
            }
            case SET -> {
                if (delta == null) return;
                ChatMetaNode node = (ChatMetaNode) delta[0];
                if (!isSingle) {
                    data.clear(NodeType.SUFFIX::matches);
                    data.add(node);
                } else {
                    Collection<SuffixNode> nodes = group != null
                            ? group.getNodes(NodeType.SUFFIX)
                            : user.getNodes(NodeType.SUFFIX);
                    nodes.stream()
                            .max(Comparator.comparingInt(ChatMetaNode::getPriority))
                            .ifPresent(data::remove);
                    data.add(node);
                }
            }
            case REMOVE -> {
                if (isSingle) {
                    Skript.warning("Cannot remove a single suffix — use reset to clear or 'suffixes' to remove specific ones.");
                    return;
                }
                if (delta == null) return;
                ChatMetaNode node = (ChatMetaNode) delta[0];
                data.remove(node);
            }
            case RESET -> data.clear(NodeType.SUFFIX::matches);
        }
    }


    @Override
    public boolean isSingle() {
        return isSingle;
    }

    @Override
    public Class<? extends ChatMetaNode> getReturnType() {
        return ChatMetaNode.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return new SyntaxStringBuilder(event, b)
                .append(isSingle ? "suffix" : "suffixes")
                .append("of")
                .append(groupExpr != null ? groupExpr : userExpr)
                .toString();
    }
}