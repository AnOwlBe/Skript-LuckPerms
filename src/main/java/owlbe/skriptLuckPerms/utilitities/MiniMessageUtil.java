package owlbe.skriptLuckPerms.utilitities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniMessageUtil {
    public static Component minimessage(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }
}
