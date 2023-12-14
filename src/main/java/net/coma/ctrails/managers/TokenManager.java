package net.coma.ctrails.managers;

import net.coma.ctoken.CToken;
import net.coma.ctoken.language.MessageKeys;
import net.coma.ctrails.CTrails;
import net.coma.ctrails.enums.Trails;
import net.coma.ctrails.interfaces.IToken;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TokenManager implements IToken {
    @Override
    public boolean hasEnoughToken(@NotNull Player player, int money) {
        return CToken.getDatabaseManager().getBalance(player) >= money;
    }

    @Override
    public boolean hasTrail(@NotNull Player player, @NotNull Trails trails) {
        return CTrails.getDatabaseManager().getTrails(player).contains(trails);
    }

    @Override
    public void tryToBuyTrail(@NotNull Player player, @NotNull Trails trails) {
        if (hasTrail(player, trails)) return;

        if (!hasEnoughToken(player, trails.getPrice())) {
            player.sendMessage(MessageKeys.NOT_ENOUGH_MONEY);
            return;
        }

        CToken.getDatabaseManager().takeFromBalance(player, trails.getPrice());
        CTrails.getDatabaseManager().addTrail(player, trails);
        CTrails.getDatabaseManager().setActiveTrail(player, trails);
        player.sendMessage(net.coma.ctrails.language.MessageKeys.BUY_TRAIL);
    }

    @Override
    public void buyTrail(@NotNull Player player, @NotNull Trails trails) {
        if (hasTrail(player, trails)) CTrails.getDatabaseManager().setActiveTrail(player, trails);
        tryToBuyTrail(player, trails);
    }

    @Override
    public void sellTrail(@NotNull Player player, @NotNull Trails trails) {
        if (!hasTrail(player, trails)) {
            player.sendMessage(net.coma.ctrails.language.MessageKeys.DONT_HAVE);
            return;
        }

        CToken.getDatabaseManager().addToBalance(player, trails.getSellPrice());
        CTrails.getDatabaseManager().sellTrail(player, trails);
        player.sendMessage(net.coma.ctrails.language.MessageKeys.SELL);
    }
}
