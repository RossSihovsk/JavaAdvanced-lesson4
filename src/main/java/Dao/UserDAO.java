package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Main.User;
import org.apache.log4j.Logger;
public class UserDAO {

    private Logger log = Logger.getLogger(UserDAO.class);

    public User insert(String firstName, String lastName, String email, String password, String accessLevel)
            throws Exception {
        log.info("Creating new user in database...");
        String query = "insert into user(`first_name`, `last_name`, `email`, `password`, `access_level`) values (?, ?, ?, ?, ?)";

        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            log.trace("Opening connection...");
            connection = DaoConnection.getInstance().getConnection();
            log.trace("Creating prepared statement...");

            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, accessLevel);
            int rows = statement.executeUpdate();
            System.out.printf("%d row(s) added!\n", rows);

            if (rows == 0) {
                log.error("Creating user failed, no rows affected!");
                throw new Exception("Creating user failed, no rows affected!");
            } else {
                resultSet = statement.getGeneratedKeys();

                if (resultSet.next()) {
                    log.trace("Creating Main.User to return...");
                    user = new User(resultSet.getInt(1), firstName, lastName, email, password, accessLevel);
                }
            }
        } catch (SQLException e) {
            log.error("Creating user failed!");
            throw new Exception("Creating user failed!", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                log.error("Result set can't be closed!", e);
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                log.error("Prepared statement can't be closed!", e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Connection can't be closed!", e);
            }
        }

        log.trace("Returning Main.User...");
        log.info(user + " is added to database!");
        return user;
    }

    public boolean delete(int id) throws Exception {
        log.info("Deleting user by id from database...");
        String sqlQuery = "delete from user where id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = false;

        try {
            log.trace("Opening connection...");
            connection = DaoConnection.getInstance().getConnection();

            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            System.out.printf("%d row(s) deleted!", rows);

            if (rows == 0) {
                log.error("Deleting user failed, no rows affected!");
                throw new Exception("Deleting user failed, no rows affected!");
            } else {
                result = true;
            }
        } catch (SQLException e) {
            log.error("Deleting user failed!");
            throw new Exception("Deleting user failed!", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                log.error("Prepared statement can't be closed!", e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Connection can't be closed!", e);
            }
        }

        if (result == false) {
            log.info("Deleting user failed, no rows affected!");
        } else {
            log.trace("Returning result...");
            log.info("Main.User with ID#" + id + " is deleted from database!");
        }
        return result;
    }



    public User readByID(int id) throws Exception {
        log.info("Getting user by id from database...");
        String sqlQuery = "select * from user where id = ?";

        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DaoConnection.getInstance().getConnection();
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                user = new User(resultSet.getInt("id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getString("email"), resultSet.getString("password"),
                        resultSet.getString("access_level"));
            }
        } catch (SQLException e) {
            log.error("Getting user by id failed!");
            throw new Exception("Getting user by id failed!", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                log.error("Result set can't be closed!", e);
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                log.error("Prepared statement can't be closed!", e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Connection can't be closed!", e);
            }
        }
        log.trace("Returning Main.User...");
        log.info(user + " is got from database!");
        return user;
    }

}