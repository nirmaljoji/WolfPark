package service.helpers;
import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;

// Class ConnectionHelper is used to create and manage database connections.
public class ConnectionHelper {

    // Method to establish and return a database connection.
    public Connection getConnection() throws Exception{

        // Loading and registering the JDBC driver for MariaDB.
        Class.forName("org.mariadb.jdbc.Driver");

        // Establishing a connection to the database using credentials from DatabaseConfig.
        // DatabaseConfig contains parameters like JDBC URL, User ID, and Password.
        return(DriverManager.getConnection(DatabaseConfig.jdbcURL, DatabaseConfig.UnityID, DatabaseConfig.SqlPassword));
    }
}

