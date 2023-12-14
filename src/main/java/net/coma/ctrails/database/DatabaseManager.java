package net.coma.ctrails.database;

import net.coma.ctrails.enums.Trails;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class DatabaseManager {

    public abstract boolean isConnected();

    public abstract void disconnect();

    public abstract void createTable();

    public abstract void createPlayer(@NotNull OfflinePlayer player);

    public abstract boolean exists(@NotNull OfflinePlayer player);

    public abstract void addTrail(@NotNull OfflinePlayer player, @NotNull Trails trails);

    public abstract List<Trails> getTrails(@NotNull OfflinePlayer player);

    public abstract void setActiveTrail(@NotNull OfflinePlayer player, @NotNull Trails trails);

    public abstract boolean isActiveTrail(@NotNull OfflinePlayer player, @NotNull Trails trail);

    public abstract boolean isThereActiveTrail(@NotNull OfflinePlayer player);

    public abstract void clearTrails(@NotNull OfflinePlayer player);

    public abstract Trails getActiveTrail(@NotNull OfflinePlayer player);

    public abstract String getActiveTrailName(@NotNull OfflinePlayer player);

    public abstract void sellTrail(@NotNull OfflinePlayer player, @NotNull Trails trails);

    public abstract void reconnect(@NotNull ConfigurationSection section);
}
