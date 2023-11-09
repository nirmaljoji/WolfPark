package service.helpers;
import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {

    public Connection getConnection() throws Exception{

        Class.forName("org.mariadb.jdbc.Driver");
        return(DriverManager.getConnection(DatabaseConfig.jdbcURL, DatabaseConfig.UnityID, DatabaseConfig.SqlPassword));
    }
}

