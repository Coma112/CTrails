package net.coma.ctrails.language;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.utils.ConfigUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Language extends ConfigUtils {
    public Language() {
        super(CTrails.getInstance().getDataFolder().getPath() + File.separator + "locales", "messages_en");

        YamlConfiguration yml = getYml();

        yml.addDefault("prefix", "&b&lTRAIL &8| ");
        yml.addDefault("messages.no-permission", "&cYou do not have permission to do this!");
        yml.addDefault("messages.reload", "&aI have successfully reloaded the files!");
        yml.addDefault("messages.player-required", "&cPlayer is required!");
        yml.addDefault("messages.buy-trail", "&aYou have successfully purchased this trail!");
        yml.addDefault("messages.dont-have", "&cYou don't have this!");
        yml.addDefault("messages.sell", "&aSuccessful sell!");
        yml.addDefault("messages.not-enough-money", "&cYou don't have enough token!");
        yml.options().copyDefaults(true);
        save();
    }
}
