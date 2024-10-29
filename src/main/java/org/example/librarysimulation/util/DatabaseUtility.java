package org.example.librarysimulation.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtility {
    private static final String URL = "jdbc:mysql://localhost:3306/booklibrary?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "27122000@ziko";

    private static DatabaseUtility instance;
    private HikariDataSource dataSource;

    private DatabaseUtility() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Retrieves a connection from the pool.
     *
     * @return A Connection object.
     * @throws SQLException If a database access error occurs.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Retrieves the singleton instance of DatabaseUtility.
     *
     * @return The DatabaseUtility instance.
     * @throws SQLException If a database access error occurs.
     */
    public static DatabaseUtility getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseUtility();
        }
        return instance;
    }

    /**
     * Closes the data source and releases all resources.
     */
    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
