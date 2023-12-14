package net.coma.ctrails.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.coma.ctrails.CTrails;
import net.coma.ctrails.enums.Trails;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MySQL extends DatabaseManager {
    private final Connection connection;

    public MySQL(@NotNull ConfigurationSection section) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();

        String host = section.getString("host");
        String database = section.getString("database");
        String user = section.getString("username");
        String pass = section.getString("password");
        int port = section.getInt("port");
        boolean ssl = section.getBoolean("ssl");
        boolean certificateVerification = section.getBoolean("certificateverification");
        int poolSize = section.getInt("poolsize");
        int maxLifetime = section.getInt("lifetime");

        hikariConfig.setPoolName("TrailPool");
        hikariConfig.setMaximumPoolSize(poolSize);
        hikariConfig.setMaxLifetime(maxLifetime * 1000L);
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pass);
        hikariConfig.addDataSourceProperty("useSSL", String.valueOf(ssl));
        if (!certificateVerification) hikariConfig.addDataSourceProperty("verifyServerCertificate", String.valueOf(false));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "275");
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        connection = dataSource.getConnection();
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS trails (PLAYER VARCHAR(255) NOT NULL, TRAIL_ID VARCHAR(255), ACTIVE_TRAIL VARCHAR(255) DEFAULT NULL, PRIMARY KEY (PLAYER))";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void createPlayer(@NotNull OfflinePlayer player) {
        String query = "INSERT IGNORE INTO trails (PLAYER) VALUES (?)";

        try {
            if (!exists(player)) {
                try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                    preparedStatement.setString(1, player.getName());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean exists(@NotNull OfflinePlayer player) {
        String query = "SELECT * FROM trails WHERE PLAYER = ?";

        try {
            if (!getConnection().isValid(2))
                reconnect(Objects.requireNonNull(CTrails.getInstance().getConfiguration().getSection("database")));

            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, player.getName());

                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void addTrail(@NotNull OfflinePlayer player, @NotNull Trails trail) {
        String query = "SELECT TRAIL_ID FROM trails WHERE PLAYER = ?";
        String query1 = "UPDATE trails SET TRAIL_ID = ? WHERE PLAYER = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, player.getName());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String trails = resultSet.getString("TRAIL_ID");
                    if (trails == null || trails.isEmpty()) trails = String.valueOf(trail.getId());
                    trails += "," + trail.getId();

                    try (PreparedStatement updateStatement = getConnection().prepareStatement(query1)) {
                        updateStatement.setString(1, trails);
                        updateStatement.setString(2, player.getName());
                        updateStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<Trails> getTrails(@NotNull OfflinePlayer player) {
        List<Trails> trailList = new ArrayList<>();
        String query = "SELECT TRAIL_ID FROM trails WHERE PLAYER = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, player.getName());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String trails = resultSet.getString("TRAIL_ID");

                    if (trails != null && !trails.isEmpty()) {
                        for (String id : trails.split(",")) {
                            Trails trail = Trails.parseId(Integer.parseInt(id.trim()));
                            if (trail != null) trailList.add(trail);
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return trailList;
    }

    @Override
    public void setActiveTrail(@NotNull OfflinePlayer player, @NotNull Trails trails) {
        String query = "UPDATE trails SET ACTIVE_TRAIL = ? WHERE PLAYER = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, trails.getName());
                preparedStatement.setString(2, player.getName());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean isActiveTrail(@NotNull OfflinePlayer player, @NotNull Trails trails) {
        String query = "SELECT ACTIVE_TRAIL FROM trails WHERE PLAYER = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, player.getName());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String activeTrail = resultSet.getString("ACTIVE_TRAIL");
                    return activeTrail != null && activeTrail.equals(trails.getName());
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return false;
    }

    @Override
    public boolean isThereActiveTrail(@NotNull OfflinePlayer player) {
        String query = "SELECT ACTIVE_TRAIL FROM trails WHERE PLAYER = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, player.getName());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String activeSound = resultSet.getString("ACTIVE_TRAIL");
                    return activeSound != null;
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return false;
    }

    @Override
    public void clearTrails(@NotNull OfflinePlayer player) {
        String query = "UPDATE trails SET ACTIVE_TRAIL = ? WHERE PLAYER = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setNull(1, Types.VARCHAR);
                preparedStatement.setString(2, player.getName());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Trails getActiveTrail(@NotNull OfflinePlayer player) {
        String query = "SELECT ACTIVE_TRAIL FROM trails WHERE PLAYER = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, player.getName());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String trailName = resultSet.getString("ACTIVE_TRAIL");

                    if (trailName != null) {
                        for (Trails trails : Trails.values()) {
                            if (trails.getName().equalsIgnoreCase(trailName)) return trails;
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return null;
    }

    @Override
    public String getActiveTrailName(@NotNull OfflinePlayer player) {
        String query = "SELECT ACTIVE_TRAIL FROM trail WHERE PLAYER = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, player.getName());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return resultSet.getString("ACTIVE_TRAIL");
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return null;
    }

    @Override
    public void sellTrail(@NotNull OfflinePlayer player, @NotNull Trails trails) {
        String query = "SELECT TRAIL_ID, ACTIVE_TRAIL FROM trails WHERE PLAYER = ?";
        String query1 = "UPDATE trails SET TRAIL_ID = ? WHERE PLAYER = ?";
        String query2 = "UPDATE trails SET ACTIVE_TRAIL = ? WHERE PLAYER = ?";

        try {
            try (PreparedStatement selectStatement = getConnection().prepareStatement(query)) {
                selectStatement.setString(1, player.getName());
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    String trail = resultSet.getString("TRAIL_ID");
                    String activeTrail = resultSet.getString("ACTIVE_TRAIL");

                    if (trail != null && !trail.isEmpty()) {
                        List<String> updatedSounds = new ArrayList<>();
                        String[] ids = trail.split(",");

                        for (String id : ids) {
                            Trails existingTrail = Trails.parseId(Integer.parseInt(id.trim()));
                            if (existingTrail != null && !existingTrail.equals(trails)) updatedSounds.add(id);
                        }

                        String updatedSoundsString = String.join(",", updatedSounds);

                        try (PreparedStatement updateStatement = getConnection().prepareStatement(query1)) {
                            updateStatement.setString(1, updatedSoundsString);
                            updateStatement.setString(2, player.getName());
                            updateStatement.executeUpdate();

                            if (activeTrail != null && activeTrail.equalsIgnoreCase(trails.getName())) {
                                try (PreparedStatement clearActiveEffectStatement = getConnection().prepareStatement(query2)) {
                                    clearActiveEffectStatement.setNull(1, Types.VARCHAR);
                                    clearActiveEffectStatement.setString(2, player.getName());
                                    clearActiveEffectStatement.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }


    @Override
    public void reconnect(@NotNull ConfigurationSection section) {
        try {
            if (getConnection() != null && !getConnection().isClosed()) getConnection().close();
            new MySQL(Objects.requireNonNull(CTrails.getInstance().getConfiguration().getSection("database.mysql")));
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to reconnect to the database", exception);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
