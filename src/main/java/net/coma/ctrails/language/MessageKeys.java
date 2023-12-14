package net.coma.ctrails.language;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.processor.MessageProcessor;
import org.jetbrains.annotations.NotNull;

public class MessageKeys {
    public static String PREFIX = getString("prefix");
    public static String NO_PERMISSION = PREFIX + getString("messages.no-permission");
    public static String RELOAD = PREFIX + getString("messages.reload");
    public static String PLAYER_REQUIRED = PREFIX + getString("messages.player-required");
    public static String BUY_TRAIL = PREFIX + getString("messages.buy-trail");
    public static String DONT_HAVE = PREFIX + getString("messages.dont-have");
    public static String SELL = PREFIX + getString("messages.sell");
    public static String NOT_ENOUGH_MONEY = PREFIX + getString("messages.not-enough-money");

    private static String getString(@NotNull String path) {
        return MessageProcessor.process(CTrails.getInstance().getLanguage().getString(path));
    }
}
