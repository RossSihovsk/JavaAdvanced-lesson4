package Dao;

import Main.Journal;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class JournalDAO {

    private Logger log = Logger.getLogger(JournalDAO.class);

    public Journal insert(String title, String description, LocalDate publishDate, int subscribePrice)
            throws Exception {
        log.info("Creating new magazine in database...");
        String query = "insert into magazine(`title`, `description`, `publish_date`, `subscribe_price`) values (?, ?, ?, ?)";

        Journal journal = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            log.trace("Opening connection...");
            connection = DaoConnection.getInstance().getConnection();

            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setDate(3, Date.valueOf(publishDate));
            statement.setInt(4, subscribePrice);

            log.trace("Executing database update...");
            int rows = statement.executeUpdate();
            System.out.printf("%d row(s) added!\n", rows);

            if (rows == 0) {
                log.error("Creating magazine failed, no rows affected!");
                throw new Exception("Creating magaziner failed, no rows affected!");
            } else {
                resultSet = statement.getGeneratedKeys();

                if (resultSet.next()) {
                    journal = new Journal(resultSet.getInt(1), title, description, publishDate, subscribePrice);
                }
            }
        } catch (SQLException e) {
            log.error("Creating magazine failed!");
            throw new Exception("Creating magazine failed!", e);

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
                log.trace("Prepared statement is closed!");
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

        log.trace("Returning Magazine...");
        log.info(journal + " is added to database!");
        return journal;
    }

    public boolean delete(int id) throws Exception {
        log.info("Deleting magazine by id from database...");
        String sqlQuery = "delete from magazine where id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = false;

        try {
            connection = DaoConnection.getInstance().getConnection();

            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            System.out.printf("%d row(s) deleted!\n", rows);

            if (rows == 0) {
                throw new Exception("Deleting magazine failed, no rows affected!");
            } else {
                result = true;
            }
        } catch (SQLException e) {
            log.error("Deleting magazine failed!");
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
            log.info("Deleting magazine failed, no rows affected!");
        } else {
            log.trace("Returning result...");
            log.info("Magazine with ID#" + id + " is deleted from database!");
        }
        return result;
    }



    public Journal readByID(int id) throws Exception {
        String sqlQuery = "select * from magazine where id = ?";

        Journal journal = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DaoConnection.getInstance().getConnection();
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                journal = new Journal(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("description"), resultSet.getDate("publish_date").toLocalDate(),
                        resultSet.getInt("subscribe_price"));
            }
        } catch (SQLException e) {
            log.error("Getting magazine by id failed!");
            throw new Exception("Getting  by id failed!", e);
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

        System.out.println(journal + " is getted from database!");
        return journal;
    }

}
