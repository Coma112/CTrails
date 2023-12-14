package net.coma.ctrails.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.coma.ctrails.CTrails;
import net.coma.ctrails.processor.MessageProcessor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "ctr";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Coma112";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(@NotNull Player player, @NotNull String params) {

        switch (params) {
            case "active_trail" -> {
                if (CTrails.getDatabaseManager().isThereActiveTrail(player)) return MessageProcessor.process(CTrails.getDatabaseManager().getActiveTrailName(player));
                else return "Nincsen aktív!";
            }

            case "is_there_active_trail" -> {
                return CTrails.getDatabaseManager().isThereActiveTrail(player) ? "Yes" : "No";
            }
        }

        return null;
    }
}
