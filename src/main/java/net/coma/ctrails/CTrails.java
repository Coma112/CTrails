package net.coma.ctrails;

import lombok.Getter;
import net.coma.ctrails.config.Config;
import net.coma.ctrails.database.DatabaseManager;
import net.coma.ctrails.database.MySQL;
import net.coma.ctrails.hooks.Placeholders;
import net.coma.ctrails.language.Language;
import net.coma.ctrails.managers.TokenManager;
import net.coma.ctrails.utils.CommandRegister;
import net.coma.ctrails.utils.ListenerRegister;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public final class CTrails extends JavaPlugin {
    @Getter
    private static CTrails instance;
    @Getter
    private static DatabaseManager databaseManager;
    private static Language language;
    private static Config config;
    private static TokenManager token;

    @Override
    public void onEnable() {
        instance = this;

        initializeComponents();
        registerEventsAndCommands();
        initializeDatabaseManager();

        MySQL mysql = (MySQL) databaseManager;
        mysql.createTable();
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) databaseManager.disconnect();
    }

    public Language getLanguage() {
        return language;
    }

    public Config getConfiguration() {
        return config;
    }

    public TokenManager getToken() {
        return token;
    }

    private void initializeComponents() {
        language = new Language();
        config = new Config();
        token = new TokenManager();
    }

    private void registerEventsAndCommands() {
        ListenerRegister.registerEvents();
        CommandRegister.registerCommands();
        new Placeholders().register();
    }

    private void initializeDatabaseManager() {
        try {
            databaseManager = new MySQL(Objects.requireNonNull(getConfiguration().getSection("database.mysql")));
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
