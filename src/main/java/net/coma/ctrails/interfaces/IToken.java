package net.coma.ctrails.interfaces;

import net.coma.ctrails.enums.Trails;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IToken {
    boolean hasEnoughToken(@NotNull Player player, int money);

    boolean hasTrail(@NotNull Player player, @NotNull Trails trails);

    void tryToBuyTrail(@NotNull Player player, @NotNull Trails trails);

    void buyTrail(@NotNull Player player, @NotNull Trails trails);

    void sellTrail(@NotNull Player player, @NotNull Trails trails);
}
