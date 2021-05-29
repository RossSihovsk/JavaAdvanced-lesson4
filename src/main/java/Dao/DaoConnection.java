package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;
import org.apache.log4j.Logger;


public class DaoConnection {
    private static DaoConnection instance;
    private Connection connection;

    private static Logger log = Logger.getLogger(DaoConnection.class);

    private String url = "jdbc:mysql://localhost/magazine_shop?serverTimezone=" + TimeZone.getDefault().getID();
    private String user = "root";
    private String password = "qwerty";

    private DaoConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            log.error("Database driver class can't be found!", e);
        } catch (SQLException e) {
            log.error("Database connection creation failed!", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DaoConnection getInstance() {
        if (instance == null) {
            log.trace("Opening connection...");
            instance = new DaoConnection();
        } else
            try {
                if (instance.getConnection().isClosed()) {
                    instance = new DaoConnection();
                    log.trace("Reopening connection...");
                }
            } catch (SQLException e) {
                log.error("Database access error!", e);
            }

        return instance;
    }
}