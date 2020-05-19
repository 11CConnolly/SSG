/*
* @ description
* Utility class for assisting in IO and database input output
 */
package cc14g17.SECdefects;

import java.sql.*;


public class IO {

    /** Constants to be able to connect to a database */
    private static String dbUrl = "jdbc:sqlite:./src/main/resources/test.db";
    private static String dbUsername = "root";
    private static String dbPassword = "root";

    public static void printString(String string) {
        System.out.println(string);
    }

    public static void printLine(String line) {
        System.out.println(line);
    }

    public static void printInt(int intIn) {
        printLine(String.format("%02d", intIn));
    }

    public static void printDouble(double doubleIn) {
        printLine(String.format("%02f", doubleIn));
    }

    /**
     * Connects to the in-memory database with predefined credentials
     *
     * @return Connection
     */
    public static Connection getDBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}
