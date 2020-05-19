/*
 * @Description
 * CWE_ID: 89
 * CWE_Entry_Name: Improper Neutralization of Special Elements used in an SQL Command ('SQL Injection')
 *
 * The program constructs all or part of an SQL command using external input from an
 * but does not neutralize special elements that could modify the intended SQL command
 */

package cc14g17.SECdefects;

import java.sql.*;

public class CWE89_SQL_Injection extends AbstractDefectiveProgram {

    /** Flag to indicate if a login attempt has been successful. */
    private boolean loggedIn;

    CWE89_SQL_Injection() {
        loggedIn = false;
    }

    @Override
    public void bad() {
        badLogin("admin", "admin");
    }

    @Override
    public void good() {
        goodLogin("admin", "admin");
    }

    /**
     * Allows user to login by matching supplied username and password to table of login details.
     * Dynamically builds query using supplied data without prepared statements allowing for SQL Injection.
     *
     * @param username
     * @param password
     */
    public void badLogin(String username, String password) {

        // returns if no username or password is supplied
        if (username == null || password == null) {
            IO.printLine("Please write a username of password");
            return;
        }

        setupDB();
        IO.printLine("Database created successfully");

        Connection connection = null;

        try {
            connection = IO.getDBConnection();

            /* FLAW Code uses dynamically created statements from user inputs */
            String sql = "SELECT * FROM users WHERE username = '" + username + "'AND password = '" + password + "'";

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(10);

            ResultSet rs = statement.executeQuery(sql);

            // At least one result means the username and password is valid
            if (rs.next())
            {
                loggedIn = true;
                // read the result set
                IO.printLine("id = " + rs.getString("id"));
                IO.printLine("username = " + rs.getString("username"));
                IO.printLine("password = " + rs.getString("password"));
            } else {
                IO.printLine("Bad username or password");
            }

            statement.close();
            rs.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                IO.printLine(e.getMessage());
            }
        }
    }

    /**
     * Allows user to login by matching supplied username and password to table of login details.
     * Dynamically builds query using supplied data using prepared statements preventing SQL Injection.
     *
     * @param username
     * @param password
     */
    public void goodLogin(String username, String password) {

        // returns if no username or password is supplied
        if (username == null || password == null) {
            IO.printLine("Please input a username or password");
            return;
        }

        setupDB();
        IO.printLine("Database created successfully");

        Connection connection = null;

        try {
            connection = IO.getDBConnection();

            // FIX Code uses prepared statements for dynamic queries
            String sql = "SELECT * FROM users WHERE username=? AND password=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setQueryTimeout(10);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();

            // At least one result means the username and password is valid
            if (rs.next())
            {
                loggedIn = true;
                // read the result set
                IO.printLine("id = " + rs.getString("id"));
                IO.printLine("username = " + rs.getString("username"));
                IO.printLine("password = " + rs.getString("password"));
            } else {
                IO.printLine("Bad username or password");
            }

            preparedStatement.close();
            rs.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                IO.printLine(e.getMessage());
            }
        }
    }

    /**
     * Gets database connection, connected class to database and performs adds necessary information
     * to enable testing and functionality of program
     */
    private void setupDB() {

        Connection connection = null;
        loggedIn = false;

        try {

            Class.forName("org.sqlite.JDBC");
            // Get the connection to the database
            connection = IO.getDBConnection();
            DatabaseMetaData meta = connection.getMetaData();
            IO.printLine("Connected to database with " +  meta);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(10);

            // Create users table with username and password fields
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            statement.executeUpdate("CREATE TABLE users (" +
                    " id integer PRIMARY KEY AUTOINCREMENT," +
                    " username text NOT NULL," +
                    " password text NOT NULL)");

            // Add sample data into database
            statement.executeUpdate("INSERT INTO users (username, password) VALUES('user', 'pass')");
            statement.executeUpdate("INSERT INTO users (username, password) VALUES('admin', 'admin')");
            statement.executeUpdate("INSERT INTO users (username, password) VALUES('callum', 'connolly')");

            statement.close();

        } catch (SQLException | ClassNotFoundException e) {
            IO.printLine(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                IO.printLine(e.getMessage());
            }
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
