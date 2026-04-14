package server.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    private final String url;
    private final String username;
    private final String password;

    private DatabaseConnection() {
        Properties config = loadProperties();
        this.url = config.getProperty("db.url");
        this.username = config.getProperty("db.username");
        this.password = config.getProperty("db.password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not found", e);
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IllegalStateException("database.properties không tìm thấy trong classpath");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc database.properties", e);
        }
        return props;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
