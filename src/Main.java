import java.sql.*;

public class Main {

    static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/"+Constants.UnityID;

    public static void main(String[] args) {
        PrepareTable.createTable();
    }
}